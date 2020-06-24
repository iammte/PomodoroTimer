package org.hyperskill.pomodoro

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator


class TimerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    private val arcStartAngle = 270
    private val thicknessScale = 0.03f

    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas

    private lateinit var mCircleOuterBounds: RectF
    private lateinit var mCircleInnerBounds: RectF

    private lateinit var mCirclePaint: Paint
    private lateinit var mEraserPaint: Paint

    private var mCircleSweepAngle: Float = 0.0f

    private lateinit var mTimerAnimator: ValueAnimator

/*
fun TimerView(context: Context?) {
this(context, null)
}

fun TimerView(context: Context?, attrs: AttributeSet?) {
this(context, attrs, 0)
}
*/

    @SuppressLint("Recycle")
    fun timerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
//        super(context, attrs, defStyleAttr)
        var circleColor = Color.RED
        if (attrs != null) {
            val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TimerView)
            circleColor = ta.getColor(R.styleable.TimerView_circleColor, circleColor)
            ta.recycle()
        }
        mCirclePaint = Paint()
        mCirclePaint.isAntiAlias = true
        mCirclePaint.color = circleColor
        mEraserPaint = Paint()
        mEraserPaint.isAntiAlias = true
        mEraserPaint.color = Color.TRANSPARENT
        mEraserPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec) // Trick to make the view square
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            mBitmap.eraseColor(Color.TRANSPARENT)
            mCanvas = Canvas(mBitmap)
        }
        super.onSizeChanged(w, h, oldw, oldh)
        updateBounds()
    }

    override fun onDraw(canvas: Canvas) {
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR)
        if (mCircleSweepAngle > 0f) {
            mCanvas.drawArc(mCircleOuterBounds, arcStartAngle.toFloat(), mCircleSweepAngle, true, mCirclePaint)
            mCanvas.drawOval(mCircleInnerBounds, mEraserPaint)
        }
        canvas.drawBitmap(mBitmap, 0f, 0f, null)
    }

    fun start(secs: Int) {
        stop()

        mTimerAnimator = ValueAnimator.ofFloat(0f, 1f)
        mTimerAnimator.duration = java.util.concurrent.TimeUnit.SECONDS.toMillis(secs.toLong())
        mTimerAnimator.interpolator = LinearInterpolator()
        mTimerAnimator.addUpdateListener { animation -> drawProgress(animation.animatedValue as Float) }

        mTimerAnimator.start()
    }

    fun stop() {
        if (mTimerAnimator.isRunning) {
            mTimerAnimator.cancel()
            drawProgress(0f)
        }
    }

    private fun drawProgress(progress: Float) {
        mCircleSweepAngle = 360 * progress
        invalidate()
    }

    private fun updateBounds() {
        val thickness: Float = width * thicknessScale
        mCircleOuterBounds = RectF(0F, 0F, width.toFloat(), height.toFloat())
        mCircleInnerBounds = RectF(
                mCircleOuterBounds.left + thickness,
                mCircleOuterBounds.top + thickness,
                mCircleOuterBounds.right - thickness,
                mCircleOuterBounds.bottom - thickness)
        invalidate()
    }

}