package com.example.makeiteven2.extras

import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import com.example.makeiteven2.intefaces.IFinishTimerListener

class TimerManager(fragment: Any, private var viewToUpdate: TextView?, val modeOfOperation: String) {

    private var currentTimeToFinish: Long = 0
    private var timeText: String = ""
    private val listener: IFinishTimerListener
    private var mTimer: CountDownTimer? = null

    fun startTimer(millisToCount: Long) {
        mTimer = object : CountDownTimer(millisToCount, 1000) {
            override fun onFinish() {
                listener.onFinishTimer()
            }

            override fun onTick(millisUntilFinished: Long) {
                currentTimeToFinish = millisUntilFinished
                val h = millisUntilFinished / 1000 / 60 / 60
                val m = (millisUntilFinished / 1000 / 60) % 60
                val s = (millisUntilFinished / 1000) % 60
                when (modeOfOperation) {
                    Constants.HINTS_TIMER -> timeText = "${if (h >= 10) h else "0$h"}:${if (m >= 10) m else "0$m"}:${if (s >= 10) s else "0$s"}"
                    Constants.ARCADE_TIMER -> timeText = "${if (m >= 10) m else "0$m"}:${if (s >= 10) s else "0$s"}"
                }
                Log.v("test", timeText)
                viewToUpdate?.text = timeText
            }

        }
        mTimer?.start()
    }

    init {
        if (fragment is IFinishTimerListener) {
            listener = fragment
        } else {
            throw ClassCastException(fragment.toString() + "must implement IFinishTimer")
        }
    }


    fun addMoreTime(timeInMillis: Long) {
        mTimer?.cancel()
        startTimer(currentTimeToFinish + timeInMillis)
    }

    fun cancelTimer() {
        mTimer?.cancel()
    }
}