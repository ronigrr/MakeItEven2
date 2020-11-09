package com.yoyoG.makeiteven2.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.yoyoG.makeiteven2.R
import com.yoyoG.makeiteven2.dialogs.DialogStore
import com.yoyoG.makeiteven2.extras.Constants
import com.yoyoG.makeiteven2.intefaces.IFinishTimerListener
import com.yoyoG.makeiteven2.intefaces.IFragmentsStartsScreenListener
import com.yoyoG.makeiteven2.intefaces.IStoreDialogBtnClickedListener
import com.yoyoG.makeiteven2.managers.AnimationsManager
import com.yoyoG.makeiteven2.managers.AudioManager
import com.yoyoG.makeiteven2.managers.TimerManager
import kotlinx.android.synthetic.main.fragment_start_screen.view.*

class FragmentStartScreen : Fragment(), IFinishTimerListener, IStoreDialogBtnClickedListener {

    private lateinit var mStageModeShine: Button
    private lateinit var mArcadeModeShine: Button
    private lateinit var mTutorialShine: Button
    private lateinit var mListener: IFragmentsStartsScreenListener
    private lateinit var mStageModeBtn: Button
    private lateinit var mArcadeModeBtn: Button
    private lateinit var mTutorialBtn: Button
    private lateinit var mScoreBoardBtn: ImageButton
    private lateinit var mLogoIv: ImageView
    private lateinit var mStoreBtn: ImageView
    private lateinit var mbtnRateUs: ImageButton
    private lateinit var mDialogStore: DialogStore



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mListener.show3DotsToolBar()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start_screen, container, false)
        initTransition()
        initViews(rootView)
        initAnimations()
        initBtnOnClick()
        initStoreDialog()
        return rootView
    }

    private fun initStoreDialog() {
        mDialogStore = DialogStore(this, context!! , this.activity!!)
        Constants.liveDataCoins.value = Constants.User.coinsLeft
    }

    private fun initViews(rootView: View) {
        mStageModeBtn = rootView.btnStageMode
        mArcadeModeBtn = rootView.btnArcadeMode
        mTutorialBtn = rootView.btnTutorial
        mScoreBoardBtn = rootView.btnScoreBoard
        mLogoIv = rootView.ivGameLogo
        mStoreBtn = rootView.IBtnStoreStageMode
        mStageModeShine = rootView.btnStageModeShine
        mArcadeModeShine = rootView.btnArcadeModeShine
        mTutorialShine = rootView.btnTutorialShine
        mbtnRateUs = rootView.btnRateUs
    }

    private fun initTransition() {
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    private fun initBtnOnClick() {
        mStageModeBtn.setOnClickListener { mListener.onStartScreenFragmentButtonClicked(it) }
        mScoreBoardBtn.setOnClickListener { mListener.onStartScreenFragmentButtonClicked(it) }
        mTutorialBtn.setOnClickListener { mListener.onStartScreenFragmentButtonClicked(it) }
        mArcadeModeBtn.setOnClickListener { mListener.onStartScreenFragmentButtonClicked(it) }
        mbtnRateUs.setOnClickListener { mListener.onStartScreenFragmentButtonClicked(it) }
        mStoreBtn.setOnClickListener { mDialogStore.showStoreDialog() }
        }

    override fun onPause() {
        super.onPause()
        mDialogStore.let {it.hideStoreDialog() }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAnimations() {
        mArcadeModeBtn.apply {
            startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        }
        mStageModeBtn.apply {
            startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        }
        mTutorialBtn.apply {
            startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        }
        mScoreBoardBtn.apply {
            startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())

        }

        mStoreBtn.apply {
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
            startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        }

        mLogoIv.startAnimation(AnimationsManager.getInstance(context!!).getBounceAnimation())

        AnimationsManager.getInstance(context!!).setFadeInOutAnimation(mStageModeShine)
        AnimationsManager.getInstance(context!!).setFadeInOutAnimation(mArcadeModeShine)
        AnimationsManager.getInstance(context!!).setFadeInOutAnimation(mTutorialShine)
    }

    override fun onAttach(context: Context) {
        if (context is IFragmentsStartsScreenListener) {
            mListener = context
        } else {
            throw ClassCastException(context.toString() + "must implement IFragmentsStartsScreenCallback")
        }
        super.onAttach(context)
    }

    override fun onFinishTimer() {
    }

    override fun onStoreDialogBtnClicked(view: View) {

    }

    override fun storeDialogDismissed() {
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            AudioManager.getInstance(it).playLoopMusicForSpecificFragment(Constants.START_SCREEN_FRAGMENT_TAG)
        }
        Log.e("lifecycle", "start screen onresume")
    }
}