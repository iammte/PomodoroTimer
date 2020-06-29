package org.hyperskill.pomodoro

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.hyperskill.pomodoro.Timer.TimerState
import org.hyperskill.pomodoro.Timer.TimerView
import org.hyperskill.pomodoro.Timer.formatTimer
import org.hyperskill.pomodoro.Timer.state

//import java.util.*
lateinit var clockView: TextView

class MainActivity : AppCompatActivity(), Runnable {

    private var timerLength = 30

    private lateinit var timerView: TimerView

    private lateinit var startButton: Button
    private lateinit var resetButton: Button

    private val handler = Handler()
    private var isCounting = false
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startButton)
        resetButton = findViewById(R.id.resetButton)
        timerView = findViewById(R.id.timerView)
        clockView = findViewById(R.id.clockView)

        formatTimer(timerLength)

        startButton.setOnClickListener {
           startFun()
        }

        resetButton.setOnClickListener {
            restartFun()
        }

    }

    private fun startFun() {
        count++

        if (!isCounting) {

            state = if (count % 2 != 0) {
                TimerState.REST
            } else {
                TimerState.WORK
            }

            formatTimer(timerLength)
            isCounting = true
            timerView.start(timerLength)
            handler.postDelayed(this, 1000)

        } else {

            timerLength = 30
            formatTimer(timerLength)
            timerView.start(timerLength)

        }
    }

    private fun restartFun() {
        timerView.stop()
        isCounting = false
        timerLength = 30
        formatTimer(timerLength)
    }

    override fun run() {
        if (isCounting) {
            timerLength--
            formatTimer(timerLength)

            if (timerLength == 0) {
                isCounting = false
                timerLength = 30
                startFun()
                return
            }

            handler.postDelayed(this, 1000)
        }
    }



}