package com.example.makeiteven2.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.work.*
import com.example.makeiteven2.R
import com.example.makeiteven2.extras.*
import com.example.makeiteven2.room.DatabaseHelper
import kotlinx.android.synthetic.main.fragment_start_screen.view.*
import kotlinx.android.synthetic.main.store_dialog.*
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

class FragmentStartScreen : Fragment(){

    private lateinit var mCallBack: IFragmentsStartsScreenCallback
    private lateinit var mStageModeBtn: Button
    private lateinit var mArcadeModeBtn: Button
    private lateinit var mTutorialBtn: Button
    private lateinit var mScoreBoardBtn: ImageButton
    private lateinit var mLogoIv: ImageView
    private lateinit var mStoreBtn: ImageView

    private lateinit var uiHandler : Handler
    private lateinit var hintTimer : HintTimer

    interface IFragmentsStartsScreenCallback {
        fun onStartScreenFragmentButtonClicked(view: View)
        fun showToolBar()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mCallBack.showToolBar()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start_screen, container, false)
        mStageModeBtn = rootView.btnStageMode
        mArcadeModeBtn = rootView.btnArcadeMode
        mTutorialBtn = rootView.btnTutorial
        mScoreBoardBtn = rootView.btnScoreBoard
        mLogoIv = rootView.ivGameLogo
        mStoreBtn = rootView.btnStore


        mLogoIv.startAnimation(Animations.getBounceAnimation(context!!))
        mScoreBoardBtn.setOnTouchListener(Animations.getTouchAnimation(context!!))
        mStageModeBtn.setOnTouchListener(Animations.getTouchAnimation(context!!))
        mTutorialBtn.setOnTouchListener(Animations.getTouchAnimation(context!!))
        mArcadeModeBtn.setOnTouchListener(Animations.getTouchAnimation(context!!))

        mStageModeBtn.setOnClickListener { mCallBack.onStartScreenFragmentButtonClicked(it) }
        mScoreBoardBtn.setOnClickListener { Log.v("user",Constants.User.hintTimeStampStart) }
        mTutorialBtn.setOnClickListener { mCallBack.onStartScreenFragmentButtonClicked(it) }
        mArcadeModeBtn.setOnClickListener { mCallBack.onStartScreenFragmentButtonClicked(it) }

        uiHandler = Handler()

        mStoreBtn.setOnClickListener{
            openStoreDialog()
        }
        return rootView
    }

    private fun initTimerForDialog(tvTimer: TextView) {
        lateinit var time1 : LocalDateTime
        lateinit var time2 : LocalDateTime
        val timeBetweenInMilli : Long
        val stringTimeStamp = System.currentTimeMillis()
        val tsTemp = Timestamp(stringTimeStamp)
        val currentTsToParse = tsTemp.toString().replace(" ","T")
            .replaceAfter(".","").replace(".","")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time1 = LocalDateTime.parse(currentTsToParse)
            time2 = LocalDateTime.parse(Constants.User.hintTimeStampStart)
            Log.v("time2",Constants.User.hintTimeStampStart)
            Log.v("time1",currentTsToParse)
            timeBetweenInMilli = Constants.TIME_UNITS_FOR_HINTS_IN_MILLI - ChronoUnit.MILLIS.between(time1,time2).absoluteValue
            //TODO: somthing to work on build < 21
            hintTimer =  HintTimer(timeBetweenInMilli,tvTimer)
            hintTimer.start()
        }
        else
        {
            tvTimer.text=""
        }

    }

    private fun openStoreDialog(){
        val storeDialog = Dialog(context!!)
        storeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        storeDialog.setContentView(R.layout.store_dialog)
        storeDialog.setCancelable(true)
        if (Constants.User.isHintGiftGiven){//disable btn and set timer
            initTimerForDialog(storeDialog.tvTimer)
            storeDialog.btnGetHint.isEnabled = false
            storeDialog.btnGetHint.isClickable = false
            storeDialog.btnGetHint.setTextColor(Color.BLACK)
            storeDialog.btnGetHint.background = ContextCompat.getDrawable(context!!,R.drawable.reset_game)
            storeDialog.btnGetHint.setTextColor(Color.GRAY)
        }
        else{
            storeDialog.btnGetHint.setOnTouchListener(Animations.getTouchAnimation(context!!))
            storeDialog.btnGetHint.background = ContextCompat.getDrawable(context!!,R.drawable.game_qube_btns)
            //onClick
            storeDialog.btnGetHint.setOnClickListener {
                //add hint
                DatabaseHelper.addHints(context!!,Constants.GIFT_HINTS_TO_GIVE)
                DatabaseHelper.setGiftGiven(context!!,true)
                //disable btn
                storeDialog.btnGetHint.background = ContextCompat.getDrawable(context!!,R.drawable.reset_game)
                storeDialog.btnGetHint.isEnabled = false
                storeDialog.btnGetHint.isClickable = false
                storeDialog.btnGetHint.setTextColor(Color.GRAY)
                //save time stamp to db
                val stringTimeStamp = System.currentTimeMillis()
                val tsTemp = Timestamp(stringTimeStamp)
                val currentTsToSave = tsTemp.toString().replace(" ","T")
                    .replaceAfter(".","").replace(".","")
                DatabaseHelper.setGiftStartTimeStamp(context!!,currentTsToSave)
                Log.v("time stamp",currentTsToSave)
                //start timer with the time needed
                hintTimer = HintTimer(Constants.TIME_UNITS_FOR_HINTS_IN_MILLI,storeDialog.tvTimer).start() as HintTimer
                //start a work
                val addHintWorkRequest : WorkRequest = OneTimeWorkRequestBuilder<HintsWorker>().setInitialDelay(Constants.TIME_UNITS_FOR_HINTS_IN_MILLI,TimeUnit.MILLISECONDS).build()
                WorkManager.getInstance(context!!).enqueue(addHintWorkRequest)
            }
        }
        storeDialog.setOnDismissListener {
            storeDialog.cancel()
            hintTimer?.onFinish()
        }
        storeDialog.show()
    }

    override fun onAttach(context: Context) {
        if (context is IFragmentsStartsScreenCallback) {
            mCallBack = context
        } else {
            throw ClassCastException(context.toString() + "must implement IFragmentsStartsScreenCallback")
        }
        super.onAttach(context)
    }
}