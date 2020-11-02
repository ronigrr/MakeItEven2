package com.example.makeiteven2.managers

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.animation.doOnEnd
import com.example.makeiteven2.R
import com.example.makeiteven2.extras.SingletonHolder
import com.github.jinatonic.confetti.CommonConfetti

class AnimationsManager private constructor(private val context: Context) {
    companion object : SingletonHolder<AnimationsManager, Context>(::AnimationsManager)

    @SuppressLint("ClickableViewAccessibility")
    fun getTouchAnimation(): View.OnTouchListener {
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

    fun getBounceAnimation(): Animation =
        AnimationUtils.loadAnimation(context, R.anim.bounce)

    fun getRotateAnimation(): Animation =
        AnimationUtils.loadAnimation(context, R.anim.rotate_restart)

    fun getBounceAndShakeAnimation(): Animation =
        AnimationUtils.loadAnimation(context, R.anim.bounce_shake)

    fun getConfetti(view: ViewGroup) {
        CommonConfetti.rainingConfetti(
            view, intArrayOf(
                Color.MAGENTA,
                Color.YELLOW,
                Color.GREEN,
                Color.CYAN,
                Color.RED,
                Color.BLUE
            )
        )
            .oneShot()
    } // just send in the layout you want to show the Confetti

    fun getShakeAnimation(): Animation =
        AnimationUtils.loadAnimation(context, R.anim.shake)

    fun getScaleOutAnimation(): Animation =
        AnimationUtils.loadAnimation(context, R.anim.scale_out)

    fun getScaleInAnimation(): Animation =
        AnimationUtils.loadAnimation(context, R.anim.scale_in)

    fun getFastScaleInAnimation(): Animation =
        AnimationUtils.loadAnimation(context, R.anim.levels_scale_in)

    fun setFadeInOutAnimation(view: View) {
        val animationSet = AnimatorSet()
        animationSet.apply {
            play(ObjectAnimator.ofFloat(view, "alpha", 0.1f, 0.5f).setDuration(1500))
                .after(ObjectAnimator.ofFloat(view, "alpha", 0.5f, 0.1f).setDuration(1500))
            start()
            doOnEnd { animator -> animator.start() }
        }
    }
}


