package org.hyperskill.pomodoro

//import android.os.Handler
//import android.widget.TextView
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), Runnable {

    private var timerLength = 30

    private lateinit var mTimerView: TimerView

    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private lateinit var clockView: TextView
    private val handler = Handler()
    private var isCounting = false
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startButton)
        resetButton = findViewById(R.id.resetButton)
        mTimerView = findViewById(R.id.timer)
        clockView = findViewById(R.id.clockView)

        formatTimer(timerLength)

        startButton.setOnClickListener {
            count++
            if (!isCounting) {
                state = if (count % 2 != 0) {
                    TimerState.REST
                } else {
                    TimerState.WORK
                }
                formatTimer(timerLength)
                isCounting = true
                mTimerView.start(timerLength)
                handler.postDelayed(this, 1000)
            } else {
                timerLength = 30
                formatTimer(timerLength)
                mTimerView.start(timerLength)
            }
        }

        resetButton.setOnClickListener {
            mTimerView.stop()
            isCounting = false
            timerLength = 30
            formatTimer(timerLength)
        }


    }

    override fun run() {
        if (isCounting) {
            timerLength--
            formatTimer(timerLength)

            if (timerLength == 0) {
                isCounting = false
                timerLength = 30
                return
            }

            handler.postDelayed(this, 1000)
        }
    }

    private fun formatTimer(count: Int) {
        val remaining = count.toString().padStart(4, '0')
        val formatted = "${remaining.substring(0, 2)}:${remaining.substring(2)}"
        clockView.text = formatted
    }

}