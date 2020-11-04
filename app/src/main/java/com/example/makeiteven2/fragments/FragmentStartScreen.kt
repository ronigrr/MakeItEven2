package com.example.makeiteven2.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.makeiteven2.R
import com.example.makeiteven2.dialogs.DialogStore
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.extras.HintsWorker
import com.example.makeiteven2.intefaces.IFinishTimerListener
import com.example.makeiteven2.intefaces.IFragmentsStartsScreenListener
import com.example.makeiteven2.intefaces.IStoreDialogBtnClickedListener
import com.example.makeiteven2.managers.AnimationsManager
import com.example.makeiteven2.managers.AudioManager
import com.example.makeiteven2.managers.GoogleAddManager
import com.example.makeiteven2.managers.TimerManager
import com.example.makeiteven2.room.DatabaseHelper
import kotlinx.android.synthetic.main.fragment_start_screen.view.*
import kotlinx.android.synthetic.main.store_dialog.*
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

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

    private var timerManager: TimerManager? = null


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
        return rootView
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
        mStoreBtn.setOnClickListener {
            //openStoreDialog()
            DialogStore(this, context!!, this.activity!!).showStoreDialog()
        }

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
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        }

        mStoreBtn.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())

        mLogoIv.startAnimation(AnimationsManager.getInstance(context!!).getBounceAnimation())

        AnimationsManager.getInstance(context!!).setFadeInOutAnimation(mStageModeShine)
        AnimationsManager.getInstance(context!!).setFadeInOutAnimation(mArcadeModeShine)
        AnimationsManager.getInstance(context!!).setFadeInOutAnimation(mTutorialShine)
    }

    private fun initTimerForDialog(tvTimer: TextView) {
        lateinit var time1: LocalDateTime
        lateinit var time2: LocalDateTime
        val timeBetweenInMilli: Long
        val stringTimeStamp = System.currentTimeMillis()
        val tsTemp = Timestamp(stringTimeStamp)
        val currentTsToParse = tsTemp.toString().replace(" ", "T")
            .replaceAfter(".", "").replace(".", "")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time1 = LocalDateTime.parse(currentTsToParse)
            time2 = LocalDateTime.parse(Constants.User.coinsGivenTimeStampStart)
            Log.v("time2", Constants.User.coinsGivenTimeStampStart)
            Log.v("time1", currentTsToParse)
            timeBetweenInMilli = Constants.TIME_UNITS_FOR_HINTS_IN_MILLI - ChronoUnit.MILLIS.between(time1, time2).absoluteValue
            //TODO: something to work on build < 21
            timerManager = TimerManager(this, tvTimer, Constants.COINS_TIMER)
            timerManager?.startTimer(timeBetweenInMilli)
        } else {
            tvTimer.text = getString(R.string.claimFreeCoins)
        }

    }

    private fun openStoreDialog() {
        val storeDialog = Dialog(context!!)
        storeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        storeDialog.setContentView(R.layout.store_dialog)
        storeDialog.setCancelable(true)
        storeDialog.storeCoinsLeftTV.text = Constants.User.coinsLeft.toString()
        if (Constants.User.isCoinsGiftGiven) {//disable btn and set timer
            initTimerForDialog(storeDialog.tvTimer)
            storeDialog.btnGetHint.isEnabled = false
            storeDialog.btnGetHint.isClickable = false
            storeDialog.btnGetHint.setTextColor(Color.BLACK)
            storeDialog.btnGetHint.background = ContextCompat.getDrawable(context!!, R.drawable.reset_game)
            storeDialog.btnGetHint.setTextColor(Color.GRAY)
            storeDialog.btnGetHint.text = getString(R.string.free_coins_in)
        } else {
            storeDialog.btnGetHint.setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
            storeDialog.btnGetHint.background = ContextCompat.getDrawable(context!!, R.drawable.free_hints_btn)
            //onClick
            storeDialog.btnGetHint.setOnClickListener {
                //add hint
                DatabaseHelper.addCoins(context!!, Constants.GIFT_HINTS_TO_GIVE)
                DatabaseHelper.setGiftGiven(context!!, true)
                //disable btn
                storeDialog.btnGetHint.background = ContextCompat.getDrawable(context!!, R.drawable.reset_game)
                storeDialog.btnGetHint.text = getString(R.string.free_coins_in)
                storeDialog.btnGetHint.isEnabled = false
                storeDialog.btnGetHint.isClickable = false
                storeDialog.btnGetHint.setTextColor(Color.GRAY)
                //save time stamp to db
                val stringTimeStamp = System.currentTimeMillis()
                val tsTemp = Timestamp(stringTimeStamp)
                val currentTsToSave = tsTemp.toString().replace(" ", "T")
                    .replaceAfter(".", "").replace(".", "")
                DatabaseHelper.setGiftStartTimeStamp(context!!, currentTsToSave)
                Log.v("time stamp", currentTsToSave)
                //start timer with the time needed
                timerManager = TimerManager(this, storeDialog.tvTimer, Constants.COINS_TIMER)
                timerManager?.startTimer(Constants.TIME_UNITS_FOR_HINTS_IN_MILLI)
                //start a work
                val addHintWorkRequest: WorkRequest =
                    OneTimeWorkRequestBuilder<HintsWorker>().setInitialDelay(Constants.TIME_UNITS_FOR_HINTS_IN_MILLI, TimeUnit.MILLISECONDS).build()
                WorkManager.getInstance(context!!).enqueue(addHintWorkRequest)
            }
        }
        storeDialog.setOnDismissListener {
            storeDialog.cancel()
            timerManager?.cancelTimer()
        }
        storeDialog.btnGetHintByAd.setOnClickListener {
            GoogleAddManager.showRewardVideo(context!!, this.activity!!)
        }
        storeDialog.show()
    }

    override fun onResume() {
        super.onResume()
        AudioManager.getInstance(context!!).playLoopMusicForSpecificFragment(Constants.START_SCREEN_FRAGMENT_TAG)
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
}