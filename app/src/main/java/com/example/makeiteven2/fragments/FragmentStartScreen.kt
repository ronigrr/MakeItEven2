package com.example.makeiteven2.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.makeiteven2.R
import kotlinx.android.synthetic.main.fragment_start_screen.view.*

class FragmentStartScreen : Fragment() {

    private lateinit var callBack : OnButtonClicked
    private lateinit var stageModeBtn : Button
    lateinit var arcadeModeBtn : Button
    lateinit var tutorialBtn : Button
    lateinit var scoreBoardBtn : ImageButton
    lateinit var logoIv : ImageView

    interface OnButtonClicked {
        fun onButtonClicked(view : View)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start_screen,container,false)
        stageModeBtn = rootView.btnStageMode
        arcadeModeBtn = rootView.btnArcadeMode
        tutorialBtn =rootView.btnTutorial
        scoreBoardBtn = rootView.btnScoreBoard
        logoIv = rootView.ivGameLogo

        val btPressAnimation = AnimationUtils.loadAnimation(inflater.context, R.anim.btn_pressed)
        val btnReleaseAnimation = AnimationUtils.loadAnimation(inflater.context, R.anim.btn_realeas)
        val btnTouchAnimation = OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.startAnimation(btPressAnimation)
                btPressAnimation.fillAfter = true
            }
            if (event.action == MotionEvent.ACTION_UP) {
                v.startAnimation(btnReleaseAnimation)
            }
            false
        }

        logoIv.startAnimation(AnimationUtils.loadAnimation(context,R.anim.bounce))
        scoreBoardBtn.setOnTouchListener(btnTouchAnimation)
        stageModeBtn.setOnTouchListener(btnTouchAnimation)
        tutorialBtn.setOnTouchListener(btnTouchAnimation)
        arcadeModeBtn.setOnTouchListener(btnTouchAnimation)

        stageModeBtn.setOnClickListener { callBack.onButtonClicked(it) }
        scoreBoardBtn.setOnClickListener{ callBack.onButtonClicked(it) }
        tutorialBtn.setOnClickListener { callBack.onButtonClicked(it) }
        arcadeModeBtn.setOnClickListener { callBack.onButtonClicked(it) }
        return rootView
    }

    override fun onAttach(context: Context) {
        if (context is OnButtonClicked) {
            callBack = context
        }
        else{
            throw ClassCastException(context.toString() + "must implement OnButtonClicked")
        }
        super.onAttach(context)
    }
//    companion object {
//        fun newInstance() : FragmentStartScreen {
//            return FragmentStartScreen()
//        }
//    }
}