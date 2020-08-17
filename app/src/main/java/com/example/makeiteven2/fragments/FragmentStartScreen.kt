package com.example.makeiteven2.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.makeiteven2.R
import com.example.makeiteven2.extras.*
import com.example.makeiteven2.room.DatabaseHelper
import kotlinx.android.synthetic.main.fragment_start_screen.view.*
import kotlinx.android.synthetic.main.store_dialog.*
import java.util.concurrent.TimeUnit

class FragmentStartScreen : Fragment(),SimpleCountDownTimerKotlin.OnCountDownListener {

    private lateinit var mCallBack: IFragmentsStartsScreenCallback
    private lateinit var mStageModeBtn: Button
    private lateinit var mArcadeModeBtn: Button
    private lateinit var mTutorialBtn: Button
    private lateinit var mScoreBoardBtn: ImageButton
    private lateinit var mLogoIv: ImageView
    private lateinit var mStoreBtn: ImageView

    private lateinit var storeDialog : Dialog
    private val countDownTimer = SimpleCountDownTimerKotlin(0, 30, this)
    private lateinit var uiHandler : Handler

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
        mScoreBoardBtn.setOnClickListener { mCallBack.onStartScreenFragmentButtonClicked(it) }
        mTutorialBtn.setOnClickListener { mCallBack.onStartScreenFragmentButtonClicked(it) }
        mArcadeModeBtn.setOnClickListener { mCallBack.onStartScreenFragmentButtonClicked(it) }

        storeDialog = Dialog(context!!)
        uiHandler = Handler()

        mStoreBtn.setOnClickListener{
            openStoreDialog()
        }

        return rootView
    }
    private fun openStoreDialog(){
        storeDialog = Dialog(context!!)
        storeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        storeDialog.setContentView(R.layout.store_dialog)
        storeDialog.setCancelable(true)
        storeDialog.btnGetHint.setOnClickListener {
            //DatabaseHelper.addHints(context!!,1)
            //countDownTimer.start(true)
            //countDownTimer.runOnBackgroundThread()
            //val addHintWorkRequest : WorkRequest = PeriodicWorkRequestBuilder<HintsWorker>(1,TimeUnit.HOURS,
            //15,TimeUnit.MINUTES).build()
            val addHintWorkRequest : WorkRequest = OneTimeWorkRequestBuilder<HintsWorker>().setInitialDelay(10,TimeUnit.SECONDS).build()
            WorkManager.getInstance(context!!).enqueue(addHintWorkRequest)
           //HintTimer(30*1000,storeDialog.tvTimer).start()

        }
        storeDialog.show()
    }
    override fun onAttach(context: Context) {
        if (context is IFragmentsStartsScreenCallback) {
            mCallBack = context
        } else {
            throw ClassCastException(context.toString() + "must implement OnButtonClicked")
        }
        super.onAttach(context)
    }

    override fun onCountDownActive(time: String) {
           uiHandler.post {
               if (storeDialog.isShowing)
                storeDialog.tvTimer.text = time
            }
    }

    override fun onCountDownFinished() {

    }
}