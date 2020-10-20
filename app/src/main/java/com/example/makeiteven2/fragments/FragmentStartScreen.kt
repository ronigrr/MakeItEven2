package com.example.makeiteven2.fragments

import android.annotation.SuppressLint
import android.app.Activity
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
import com.example.makeiteven2.extras.Animations
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.extras.HintsWorker
import com.example.makeiteven2.extras.TimerManager
import com.example.makeiteven2.intefaces.IFinishTimerListener
import com.example.makeiteven2.intefaces.IFragmentsStartsScreenListener
import com.example.makeiteven2.room.DatabaseHelper
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.fragment_start_screen.view.*
import kotlinx.android.synthetic.main.store_dialog.*
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

class FragmentStartScreen : Fragment(), IFinishTimerListener {

    private lateinit var mListener: IFragmentsStartsScreenListener
    private lateinit var mStageModeBtn: Button
    private lateinit var mArcadeModeBtn: Button
    private lateinit var mTutorialBtn: Button
    private lateinit var mScoreBoardBtn: ImageButton
    private lateinit var mLogoIv: ImageView
    private lateinit var mStoreBtn: ImageView

    lateinit var rewardedAd: RewardedAd

    private var timerManager: TimerManager? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mListener.show3DotsToolBar()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start_screen, container, false)
        initTransition()
        mStageModeBtn = rootView.btnStageMode
        mArcadeModeBtn = rootView.btnArcadeMode
        mTutorialBtn = rootView.btnTutorial
        mScoreBoardBtn = rootView.btnScoreBoard
        mLogoIv = rootView.ivGameLogo
        mStoreBtn = rootView.btnStore
        initAnimations()
        initBtnOnClick()
        return rootView
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
            openStoreDialog()
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAnimations() {
        mLogoIv.startAnimation(Animations.getBounceAnimation(context!!))
        mScoreBoardBtn.setOnTouchListener(Animations.getTouchAnimation(context!!))
        mStageModeBtn.setOnTouchListener(Animations.getTouchAnimation(context!!))
        mTutorialBtn.setOnTouchListener(Animations.getTouchAnimation(context!!))
        mArcadeModeBtn.setOnTouchListener(Animations.getTouchAnimation(context!!))
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
        loadRewardAD() //TODO:The loadRewardAD fun jams the ui for som reason
        val storeDialog = Dialog(context!!)
        storeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        storeDialog.setContentView(R.layout.store_dialog)
        storeDialog.setCancelable(true)
        if (Constants.User.isCoinsGiftGiven) {//disable btn and set timer
            initTimerForDialog(storeDialog.tvTimer)
            storeDialog.btnGetHint.isEnabled = false
            storeDialog.btnGetHint.isClickable = false
            storeDialog.btnGetHint.setTextColor(Color.BLACK)
            storeDialog.btnGetHint.background = ContextCompat.getDrawable(context!!, R.drawable.reset_game)
            storeDialog.btnGetHint.setTextColor(Color.GRAY)
            storeDialog.btnGetHint.text = getString(R.string.free_coins_in)
        } else {
            storeDialog.btnGetHint.setOnTouchListener(Animations.getTouchAnimation(context!!))
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
            loadRewardVideo()
        }
        storeDialog.show()
    }

    private fun loadRewardAD() {
        rewardedAd = RewardedAd(context, Constants.ADD_MOB_TEST)
        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                Log.v("ad", "onRewardedAdLoaded")
            }

            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                Log.v("ad", "onRewardedAdFailedToLoad")
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
    }

    private fun loadRewardVideo() {
        if (rewardedAd.isLoaded) {
            val activityContext: Activity = this.activity!!
            val adCallback = object : RewardedAdCallback() {
                override fun onRewardedAdOpened() {
                    Log.v("ad", "onRewardedAdOpened")
                }

                override fun onRewardedAdClosed() {
                    Log.v("ad", "onRewardedAdClosed")
                    loadRewardAD()
                }

                override fun onUserEarnedReward(reward: RewardItem) {
                    Log.v("ad", "onUserEarnedReward (${reward.amount.toString()})")
                    DatabaseHelper.addCoins(context!!, reward.amount)
                }

                override fun onRewardedAdFailedToShow(adError: AdError) {
                    Log.v("ad", "onRewardedAdFailedToShow")
                }
            }
            rewardedAd.show(activityContext, adCallback)
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.")
        }
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
}