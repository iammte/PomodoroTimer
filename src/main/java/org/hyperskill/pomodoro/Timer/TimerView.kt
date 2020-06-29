package org.hyperskill.pomodoro.Timer

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import org.hyperskill.pomodoro.R
import org.hyperskill.pomodoro.clockView

private const val arcStartAngle = 270
private const val thicknessScale = 0.05f

private lateinit var mBitmap: Bitmap
private lateinit var mCanvas: Canvas

private lateinit var mCircleOuterBounds: RectF
private lateinit var mCircleInnerBounds: RectF

private var mCirclePaint: Paint = Paint()
private var mEraserPaint: Paint = Paint()

private var mCircleSweepAngle: Float = 0.0f

private lateinit var mTimerAnimator: ValueAnimator
private var circleColor = Color.CYAN

var state: TimerState = TimerState.WORK


enum class TimerState{
    WORK,
    REST,
    STOP;
}

class TimerView @JvmOverloads constructor(context: Context, private var attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {


/*
fun timerView(context: Context?) {
this(context, null)
}

fun timerView(context: Context?, attrs: AttributeSet?) {
this(context, attrs, 0)
}


@SuppressLint("Recycle")
fun timerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
timerView()
super(context, attrs, defStyleAttr)
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
*/

    private fun timerView() {

        circleColor = when(state) {
            TimerState.WORK -> Color.RED
            TimerState.REST -> Color.GREEN
            TimerState.STOP -> Color.YELLOW
        }

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


    fun getText(): String {
        return clockView.text.toString()
    }

    fun getColor(): Int {
        return circleColor
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
        timerView()
        mTimerAnimator = ValueAnimator.ofFloat(0f, 1f)
        mTimerAnimator.duration = java.util.concurrent.TimeUnit.SECONDS.toMillis(secs.toLong())
        mTimerAnimator.interpolator = LinearInterpolator()
        mTimerAnimator.addUpdateListener { animation -> drawProgress(animation.animatedValue as Float) }

        mTimerAnimator.start()
    }

    fun stop() {
        mCirclePaint.color = Color.YELLOW
        if (mTimerAnimator.isRunning) {
            mTimerAnimator.cancel()
            drawProgress(1f)
        } else {
            mTimerAnimator.cancel()
            drawProgress(1f)
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

fun formatTimer(count: Int) {
    val remaining = count.toString().padStart(4, '0')
    val formatted = "${remaining.substring(0, 2)}:${remaining.substring(2)}"
    clockView.text = formatted
}