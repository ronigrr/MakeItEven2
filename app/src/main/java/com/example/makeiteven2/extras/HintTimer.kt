package com.example.makeiteven2.extras

import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

class HintTimer (val millisToCount : Long, var viewToUpdate : TextView?) : CountDownTimer(millisToCount,1000){
    var timeText : String = ""
    override fun onFinish() {
        this.cancel()
    }
    override fun onTick(millisUntilFinished: Long) {
        val h = millisUntilFinished / 1000 / 60 / 60
        val m = (millisUntilFinished / 1000 / 60)%60
        val s = (millisUntilFinished / 1000)%60
        timeText="${if (h>=10) h else "0$h"}:${if (m>=10) m else "0$m"}:${if (s>=10) s else "0$s"}"
        viewToUpdate?.text=timeText
        //Log.v("time",timeText)
    }

}