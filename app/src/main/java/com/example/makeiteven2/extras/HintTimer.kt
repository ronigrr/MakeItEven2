package com.example.makeiteven2.extras

import android.os.CountDownTimer
import android.widget.TextView
import androidx.fragment.app.Fragment

class HintTimer(fragment: Any, millisToCount: Long, private var viewToUpdate: TextView?, val modeOfOperation: String) :
    CountDownTimer(millisToCount, 1000) {
    private var timeText: String = ""
    private val listener: IFinishTimer

    init {
        if (fragment is IFinishTimer) {
            listener = fragment
        } else {
            throw ClassCastException(fragment.toString() + "must implement IFinishTimer")
        }
    }

    override fun onFinish() {
        listener.onFinishTimer()
    }

    override fun onTick(millisUntilFinished: Long) {
        val h = millisUntilFinished / 1000 / 60 / 60
        val m = (millisUntilFinished / 1000 / 60) % 60
        val s = (millisUntilFinished / 1000) % 60
        when (modeOfOperation) {
            Constants.HINTS_TIMER -> timeText = "${if (h >= 10) h else "0$h"}:${if (m >= 10) m else "0$m"}:${if (s >= 10) s else "0$s"}"
            Constants.ARCADE_TIMER -> timeText = "${if (m >= 10) m else "0$m"}:${if (s >= 10) s else "0$s"}"
        }

        viewToUpdate?.text = timeText
        //Log.v("time",timeText)
    }

}