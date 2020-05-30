package com.example.makeiteven2.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.makeiteven2.R
import com.example.makeiteven2.extras.OnTouchAnimation
import kotlinx.android.synthetic.main.fragment_start_screen.view.*

class FragmentStartScreen : Fragment() {

    private lateinit var callBack : IFragmentsStartsScreenCallback
    private lateinit var mStageModeBtn : Button
    private lateinit var mOnTouchAnimation : OnTouchAnimation
    private lateinit var mArcadeModeBtn : Button
    private lateinit var mTutorialBtn : Button
    private lateinit var mScoreBoardBtn : ImageButton
    private lateinit var mLogoIv : ImageView

    interface IFragmentsStartsScreenCallback {
        fun onStartScreenFragmentButtonClicked(view : View)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start_screen,container,false)
        mStageModeBtn = rootView.btnStageMode
        mArcadeModeBtn = rootView.btnArcadeMode
        mTutorialBtn =rootView.btnTutorial
        mScoreBoardBtn = rootView.btnScoreBoard
        mLogoIv = rootView.ivGameLogo
        mOnTouchAnimation = OnTouchAnimation(inflater.context)

        mLogoIv.startAnimation(AnimationUtils.loadAnimation(context,R.anim.bounce))
        mScoreBoardBtn.setOnTouchListener(mOnTouchAnimation.btnTouchAnimation)
        mStageModeBtn.setOnTouchListener(mOnTouchAnimation.btnTouchAnimation)
        mTutorialBtn.setOnTouchListener(mOnTouchAnimation.btnTouchAnimation)
        mArcadeModeBtn.setOnTouchListener(mOnTouchAnimation.btnTouchAnimation)

        mStageModeBtn.setOnClickListener { callBack.onStartScreenFragmentButtonClicked(it) }
        mScoreBoardBtn.setOnClickListener{ callBack.onStartScreenFragmentButtonClicked(it) }
        mTutorialBtn.setOnClickListener { callBack.onStartScreenFragmentButtonClicked(it) }
        mArcadeModeBtn.setOnClickListener { callBack.onStartScreenFragmentButtonClicked(it) }
        return rootView
    }

    override fun onAttach(context: Context) {
        if (context is IFragmentsStartsScreenCallback) {
            callBack = context
        }
        else{
            throw ClassCastException(context.toString() + "must implement OnButtonClicked")
        }
        super.onAttach(context)
    }
}