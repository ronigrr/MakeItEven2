package com.example.makeiteven2.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.makeiteven2.R
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.extras.HintsWorker
import com.example.makeiteven2.intefaces.IFinishTimerListener
import com.example.makeiteven2.intefaces.IStoreDialogBtnClickedListener
import com.example.makeiteven2.managers.AnimationsManager
import com.example.makeiteven2.managers.GoogleAddManager
import com.example.makeiteven2.managers.TimerManager
import com.example.makeiteven2.room.DatabaseHelper
import kotlinx.android.synthetic.main.store_dialog.*
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue


class DialogStore(fragment: Any, private val mContext: Context,private val activityContext: FragmentActivity) : IFinishTimerListener{

    private val listener: IStoreDialogBtnClickedListener
    private lateinit var mStoreDialog: Dialog
    private var timerManager: TimerManager? = null

    init {
        if (fragment is IStoreDialogBtnClickedListener) {
            listener = fragment
        } else {
            throw ClassCastException(fragment.toString() + "must implement IStoreDialogBtnClicked")
        }
    }

    fun showStoreDialog(){
        mStoreDialog = Dialog(mContext)
        mStoreDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mStoreDialog.setContentView(R.layout.store_dialog)
        mStoreDialog.setCancelable(true)
        mStoreDialog.storeCoinsLeftTV.text = Constants.User.coinsLeft.toString()
        if (Constants.User.isCoinsGiftGiven) {//disable btn and set timer
            initTimerForDialog(mStoreDialog.tvTimer)
            mStoreDialog.btnGetHint.isEnabled = false
            mStoreDialog.btnGetHint.isClickable = false
            mStoreDialog.btnGetHint.setTextColor(Color.BLACK)
            mStoreDialog.btnGetHint.background = ContextCompat.getDrawable(mContext, R.drawable.reset_game)
            mStoreDialog.btnGetHint.setTextColor(Color.GRAY)

            mStoreDialog.btnGetHint.text = activityContext.getString(R.string.free_coins_in)
        } else {
            mStoreDialog.btnGetHint.setOnTouchListener(AnimationsManager.getTouchAnimation(mContext))
            mStoreDialog.btnGetHint.background = ContextCompat.getDrawable(mContext, R.drawable.free_hints_btn)
            //onClick
            mStoreDialog.btnGetHint.setOnClickListener {
                //add hint
                DatabaseHelper.addCoins(mContext, Constants.GIFT_HINTS_TO_GIVE)
                DatabaseHelper.setGiftGiven(mContext, true)
                //disable btn
                mStoreDialog.btnGetHint.background = ContextCompat.getDrawable(mContext, R.drawable.reset_game)
                mStoreDialog.btnGetHint.text = activityContext.getString(R.string.free_coins_in)
                mStoreDialog.btnGetHint.isEnabled = false
                mStoreDialog.btnGetHint.isClickable = false
                mStoreDialog.btnGetHint.setTextColor(Color.GRAY)
                //save time stamp to db
                val stringTimeStamp = System.currentTimeMillis()
                val tsTemp = Timestamp(stringTimeStamp)
                val currentTsToSave = tsTemp.toString().replace(" ", "T")
                    .replaceAfter(".", "").replace(".", "")
                DatabaseHelper.setGiftStartTimeStamp(mContext, currentTsToSave)
                Log.v("time stamp", currentTsToSave)
                //start timer with the time needed
                timerManager = TimerManager(this, mStoreDialog.tvTimer, Constants.COINS_TIMER)
                timerManager?.startTimer(Constants.TIME_UNITS_FOR_HINTS_IN_MILLI)
                //start a work
                val addHintWorkRequest: WorkRequest =
                    OneTimeWorkRequestBuilder<HintsWorker>().setInitialDelay(Constants.TIME_UNITS_FOR_HINTS_IN_MILLI, TimeUnit.MILLISECONDS).build()
                WorkManager.getInstance(mContext).enqueue(addHintWorkRequest)
            }
        }
        mStoreDialog.setOnDismissListener {
            mStoreDialog.cancel()
            timerManager?.cancelTimer()
        }
        mStoreDialog.btnGetHintByAd.setOnClickListener {
            //TODO:: the number of hints is not updating after you get your hints (only in the view)
            GoogleAddManager.loadRewardVideo(mContext,activityContext)
        }

        mStoreDialog.btnCloseSettings.setOnClickListener {
            mStoreDialog.dismiss()
        }

        mStoreDialog.show()
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
            tvTimer.text = activityContext.getString(R.string.claimFreeCoins)
        }

    }

    override fun onFinishTimer() {

    }
}