package com.example.makeiteven2.extras

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import com.example.makeiteven2.R

class OnTouchAnimation (private var context: Context) {

    private val btPressAnimation = AnimationUtils.loadAnimation(context, R.anim.btn_pressed)
    private val btnReleaseAnimation = AnimationUtils.loadAnimation(context, R.anim.btn_realeas)

    val btnTouchAnimation = View.OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            v.startAnimation(btPressAnimation)
            btPressAnimation.fillAfter = true
        }
        if (event.action == MotionEvent.ACTION_UP) {
            v.startAnimation(btnReleaseAnimation)
        }
        false
    }
}