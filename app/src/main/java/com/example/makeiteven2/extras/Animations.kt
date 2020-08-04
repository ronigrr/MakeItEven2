package com.example.makeiteven2.extras

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.makeiteven2.R

object Animations {
        fun getTouchAnimation(context: Context): View.OnTouchListener {
            AnimationUtils.loadAnimation(context, R.anim.btn_pressed)
            val btPressAnimation = AnimationUtils.loadAnimation(context, R.anim.btn_pressed)
            val btnReleaseAnimation = AnimationUtils.loadAnimation(context, R.anim.btn_realeas)
            return View.OnTouchListener { v, event ->
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
        fun getBounceAnimation(context: Context): Animation = AnimationUtils.loadAnimation(context,R.anim.bounce)
        fun getRotateAnimation(context: Context): Animation = AnimationUtils.loadAnimation(context,R.anim.rotate_restart)
        fun getBounceAndShakeAnimation(context: Context): Animation = AnimationUtils.loadAnimation(context,R.anim.bounce_shake)

}