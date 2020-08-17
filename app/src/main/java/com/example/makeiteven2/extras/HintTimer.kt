package com.example.makeiteven2.extras

import android.os.CountDownTimer
import android.widget.TextView

class HintTimer (val millisToCount : Long ,val  viewToUpdate : TextView?) : CountDownTimer(millisToCount,1000){
    var timeText : String = ""
    override fun onFinish() {
        TODO("Not yet implemented")
    }

    override fun onTick(millisUntilFinished: Long) {
       val diff = millisUntilFinished/1000
       timeText = diff.toString()
        viewToUpdate?.text=timeText
    }

}