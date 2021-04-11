package com.yoyoG.makeiteven2.fragments

import android.animation.Animator
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup
import com.yoyoG.makeiteven2.R
import com.yoyoG.makeiteven2.dialogs.DialogEndGameManager
import com.yoyoG.makeiteven2.extras.Constants
import com.yoyoG.makeiteven2.firebase.FireBaseHelper.saveScoreToDatabaseScoreBoard
import com.yoyoG.makeiteven2.game.GameFactory
import com.yoyoG.makeiteven2.intefaces.IEndDialogBtnClickedListener
import com.yoyoG.makeiteven2.intefaces.IFinishTimerListener
import com.yoyoG.makeiteven2.intefaces.IFragmentArcadeModeListener
import com.yoyoG.makeiteven2.managers.AnimationsManager
import com.yoyoG.makeiteven2.managers.AudioManager
import com.yoyoG.makeiteven2.managers.TimerManager
import com.yoyoG.makeiteven2.room.DatabaseHelper
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_game_arcade.view.*

class FragmentArcadeModeScreen : Fragment(), View.OnClickListener, IFinishTimerListener, IEndDialogBtnClickedListener {


    private lateinit var mScoreTV: TextView
    private lateinit var mTimerManager: TimerManager
    private lateinit var mTimerTV: TextView
    private lateinit var mActualScoreTV: TextView
    private lateinit var mTargetNumberTV: TextView
    private lateinit var mBackButtonIB: ImageButton
    private lateinit var mHintIB1: ImageButton
    private lateinit var mHintIB2: ImageButton
    private lateinit var mHintIB3: ImageButton
    private lateinit var mGameButton1TB: ToggleButton
    private lateinit var mGameButton2TB: ToggleButton

    private lateinit var mGameButton3TB: ToggleButton
    private lateinit var mGameButton4TB: ToggleButton
    private lateinit var mGamePlusTB: ToggleButton
    private lateinit var mGameMinusTB: ToggleButton

    private lateinit var mGameMulTB: ToggleButton
    private lateinit var mGameDivTB: ToggleButton
    private lateinit var mOperatorGroup: SingleSelectToggleGroup
    private lateinit var mNumberGroup: SingleSelectToggleGroup

    private val mGameButtonsList = ArrayList<ToggleButton>()
    private val mOperatorsList = ArrayList<ToggleButton>()

    private lateinit var rootView: View
    private var mHintString: String = ""

    private var mScoreCounter = 0
    private var mWinsCounter = 0
    private var mLevelNum = 0
    private var mTargetNumber = 0
    private val mGame = GameFactory.getGame(Constants.STAGE_GAME_TYPE, 12)
    private var soundEffectsVolume: Float = Constants.User.soundEffectsLevel.toFloat()


    private lateinit var listener: IFragmentArcadeModeListener

    private var isNumberSelected = false
    private var isOperatorSelected = false
    private var num1 = Int.MAX_VALUE
    private var num2 = Int.MAX_VALUE
    private var selectedNumberID1 = 0
    private var selectedNumberID2 = 0
    private var operator = ""
    private var selectedOperatorID = 0
    private var rewardTimeInMillis: Long = 7 * 1000

    private lateinit var mEndGameDialog: DialogEndGameManager


    override fun onStop() {
        super.onStop()
        mTimerManager.cancelTimer()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentArcadeModeListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + "must implement IFragmentArcadeModeListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_game_arcade, container, false)
        listener.arcadeModeHide3dotToolBar()
        initToasty()
        gameSetup()
        gameInit()
        initTimer()
        initDialog()
        startCountDownAnimation()
        return rootView
    }

    private fun initTimer() {
        mTimerManager = TimerManager(this, mTimerTV, Constants.ARCADE_TIMER)
    }

    private fun initDialog() {
        mEndGameDialog = DialogEndGameManager(this, context!!)
    }

    private fun startCountDownAnimation() {
        rootView.countDownAnim.apply {
            setAnimation(R.raw.coundown321go)
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    Log.e("Animation:", "start")
                }

                override fun onAnimationEnd(animation: Animator) {
                    Log.e("Animation:", "end")
                    countDownAnim.visibility = View.GONE
                    mTimerManager.startTimer(Constants.TEST_COUNTDOWN_10_SECONDS_IN_MILLIS)
                    setButtonAnimation()
                }

                override fun onAnimationCancel(animation: Animator) {
                    Log.e("Animation:", "cancel")
                }

                override fun onAnimationRepeat(animation: Animator) {
                    Log.e("Animation:", "repeat")
                }
            })
            playAnimation()
        }


    }

    private fun setButtonAnimation() {
        mBackButtonIB.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mHintIB1.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mHintIB2.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mHintIB3.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        for (i in 0..3) {
            mGameButtonsList[i].startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
            mOperatorsList[i].startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        }
        mActualScoreTV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mTargetNumberTV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mTimerTV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mActualScoreTV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mActualScoreTV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mScoreTV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mTargetNumberTV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
    }

    private fun gameSetup() {
        initFragmentMembersFromView()
        mGameButtonsList.add(mGameButton1TB)
        mGameButtonsList.add(mGameButton2TB)
        mGameButtonsList.add(mGameButton3TB)
        mGameButtonsList.add(mGameButton4TB)
        mOperatorsList.add(mGamePlusTB)
        mOperatorsList.add(mGameMinusTB)
        mOperatorsList.add(mGameMulTB)
        mOperatorsList.add(mGameDivTB)
        setButtonsListeners()

    }

    private fun initToasty() {
        Toasty.Config.getInstance().tintIcon(false).setTextSize(30).allowQueue(true).apply()
    }

    private fun setButtonsListeners() {
        val hintListener = View.OnClickListener { view ->
            gameInit()
            (view as ImageButton).setImageResource(R.drawable.ic_help_off)
            view.isEnabled = false

        }
        mHintIB1.apply {
            setOnClickListener(hintListener)
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        }
        mHintIB2.apply {
            setOnClickListener(hintListener)
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        }
        mHintIB3.apply {
            setOnClickListener(hintListener)
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        }

        mNumberGroup.apply {
            setOnCheckedChangeListener { _, checkedId ->
                val checkedTB = findViewById<ToggleButton>(checkedId)
                if (isNumberSelected && isOperatorSelected) {
                    num2 = checkedTB.text.toString().toInt()
                    selectedNumberID2 = checkedId
                } else {
                    num1 = checkedTB.text.toString().toInt()
                    selectedNumberID1 = checkedId
                    isNumberSelected = true
                }
            }
        }
        mOperatorGroup.apply {
            setOnCheckedChangeListener { _, checkedId ->
                val checkedTB = findViewById<ToggleButton>(checkedId)
                operator = checkedTB.tag.toString()
                isOperatorSelected = true
                selectedOperatorID = checkedId
            }
        }
        for (tb in mGameButtonsList) {
            tb.apply {
                setOnClickListener(this@FragmentArcadeModeScreen)
                setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
                isEnabled = false
            }
        }
        for (tb in mOperatorsList) {
            tb.apply {
                setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
                setOnClickListener(this@FragmentArcadeModeScreen)
            }
        }

        mBackButtonIB.apply {
            setOnClickListener { listener.backButtonPressedArcade() }
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        }
    }

    private fun gameInit() {
        for (tb in mGameButtonsList) {
            tb.apply {
                visibility = View.VISIBLE
                isEnabled = true
                isChecked = false
                startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
            }
        }
        for (tb in mOperatorsList) {
            tb.apply {
                isChecked = false
            }
        }
        isNumberSelected = false
        isOperatorSelected = false

        var min = 0
        var max = 0
        var difficulty = 0
        when {
            mWinsCounter < 2 ->{
                min = 0
                max = 10
                difficulty = 5
                mGame?.updateRandomLevelFlag(mWinsCounter)
            }
            mWinsCounter < 6 -> {
                min = 10
                max = 20
                difficulty = 6
                mGame?.updateRandomLevelFlag(mWinsCounter)
            }
            mWinsCounter < 12 -> {
                min = 20
                max = 40
                difficulty = 7
                mGame?.updateRandomLevelFlag(mWinsCounter)
            }
            mWinsCounter < 16 -> {
                min = 40
                max = 60
                difficulty = 8
                mGame?.updateRandomLevelFlag(mWinsCounter)
            }
            mWinsCounter < 20 -> {
                min = 60
                max = 90
                difficulty = 9
                mGame?.updateRandomLevelFlag(mWinsCounter)
            }
            mWinsCounter < 100 -> {
                min = 80
                max = 140
                difficulty = 18
                mGame?.updateRandomLevelFlag(mWinsCounter)
            }
            else -> {
                min = 140
                max = 250
                difficulty = 25
                mGame?.updateRandomLevelFlag(mWinsCounter)
            }
        }
        mGame?.setDifficulty(difficulty)
        do {
            if (mGame != null) {
                mTargetNumber = mGame.gameGenerator(mGameButtonsList)
                mHintString = mGame.getHint()
            }
        } while (mTargetNumber > max || mTargetNumber < min)

        mTargetNumberTV.text = mTargetNumber.toString()

    }

    private fun initFragmentMembersFromView() {
        mTimerTV = rootView.timerTV // "Level: X"
        mActualScoreTV = rootView.actualScoreTV //"Hints: X"
        mScoreTV = rootView.scoreTV
        mTargetNumberTV = rootView.theTargetNumberTV
        mBackButtonIB = rootView.backButtonIB
        mHintIB1 = rootView.hintButton3IB
        mHintIB2 = rootView.hintButton2IB
        mHintIB3 = rootView.hintButtonIB
        mGameButton1TB = rootView.btn1TB
        mGameButton2TB = rootView.btn2TB
        mGameButton3TB = rootView.btn3TB
        mGameButton4TB = rootView.btn4TB
        mGamePlusTB = rootView.plusTB
        mGameMinusTB = rootView.minusTB
        mGameMulTB = rootView.multiplyTB
        mGameDivTB = rootView.divideTB

        mOperatorGroup = rootView.group_choices_of_operators
        mNumberGroup = rootView.group_choices_of_numbers


        mActualScoreTV.text = " 0"

    }

    override fun onClick(v: View?) {
        Handler(Looper.getMainLooper()).post {
            val btnOffPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.btn_off_sound)
            val btnOnPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.btn_on_sound)
            btnOnPlayer.setVolume(soundEffectsVolume, soundEffectsVolume)
            btnOffPlayer.setVolume(soundEffectsVolume, soundEffectsVolume)
            if (!(v as ToggleButton).isChecked) {
                btnOnPlayer.start()
            } else if (v.isChecked) {
                btnOffPlayer.start()
            }
        }
        /// checks that nobody checked
        var i = 0
        for (toggleButton in mGameButtonsList) {
            if (toggleButton.isChecked) break
            i++
        }
        if (i == 4) {
            num1 = Int.MAX_VALUE
            isNumberSelected = false
            selectedNumberID1 = 0
        }
        //checks that no operator checked
        i = 0
        for (toggleButton in mOperatorsList) {
            if (toggleButton.isChecked) break
            i++
        }
        if (i == 4) {
            operator = ""
            isOperatorSelected = false
            selectedOperatorID = 0
        }

        if (num2 != Int.MAX_VALUE) {
            var sum = 0
            var isDivideZero = false
            var isFraction = false
            when (operator) {
                "plus" -> sum = num1 + num2
                "minus" -> sum = num1 - num2
                "div" -> if (num2 == 0) {
                    isDivideZero = true
                } else if (num1 % num2 != 0) {
                    isFraction = true
                } else sum = num1 / num2
                "mul" -> sum = num1 * num2
            }

            //set new button
            val toggleButton: ToggleButton = rootView.findViewById(selectedNumberID2)
            toggleButton.startAnimation(AnimationsManager.getInstance(context!!).getScaleOutAnimation())
            toggleButton.textOn = sum.toString()
            toggleButton.textOff = sum.toString()
            toggleButton.text = sum.toString()
            toggleButton.isChecked = false


            //button to remove+anim
            val toggleButtonToHide: ToggleButton = rootView.findViewById(selectedNumberID1)
            toggleButtonToHide.startAnimation(AnimationsManager.getInstance(context!!).getScaleOutAnimation())
            toggleButtonToHide.visibility = View.INVISIBLE
            toggleButtonToHide.isEnabled = false


            toggleButton.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())

            val operator: ToggleButton = rootView.findViewById(selectedOperatorID)
            operator.isChecked = false

            //reset flags
            isOperatorSelected = false
            isNumberSelected = false
            num2 = Int.MAX_VALUE

            i = 0
            for (tb in mGameButtonsList) {
                if (tb.isEnabled) i++
            }
            if (isDivideZero || isFraction) {
                context?.let { AudioManager.getInstance(it).playWrongAnswerSound() }
                view?.startAnimation(AnimationsManager.getInstance(context!!).getShakeAnimation())
                mTimerManager.reduceTime(Constants.ARCADE_MODE_REDUCE_TIME_PENALTY)
                gameInit()
                return
            }

            if (i == 1) {
                //game finished
                if (mTargetNumber == sum) {
                    //you win
                    context?.let { AudioManager.getInstance(it).playArcadeSuccessSound() }
                    AnimationsManager.getInstance(context!!).getConfetti(rootView.confetti_view)
                    gameInit()
                    mTimerManager.addMoreTime(rewardTimeInMillis)
                    mWinsCounter++
                    when {
                        mWinsCounter < 5 -> mScoreCounter += 100
                        mWinsCounter < 7 -> {
                            mScoreCounter += 200
                            //adding more 12sec
                            val millisToAdd: Long = (9 * 1000)
                            rewardTimeInMillis = millisToAdd
                        }
                        else -> {
                            mScoreCounter += 300
                            val millisToAdd: Long = (12 * 1000)
                            rewardTimeInMillis = millisToAdd
                        }

                    }

                    mActualScoreTV.text = mScoreCounter.toString()

                } else {
                    context?.let { AudioManager.getInstance(it).playWrongAnswerSound() }
                    view?.startAnimation(AnimationsManager.getInstance(context!!).getShakeAnimation())
                    mTimerManager.reduceTime(Constants.ARCADE_MODE_REDUCE_TIME_PENALTY)
                    gameInit()
                }
            }

        }
    }

    override fun onFinishTimer() {
        if (mScoreCounter > Constants.User.arcadeHighScore.toInt()) {
            DatabaseHelper.setPlayerArcadeMaxScore(context!!, mScoreCounter.toString())
            saveScoreToDatabaseScoreBoard(mScoreCounter)
        }
        mEndGameDialog.showEndDialog(Constants.ARCADE_END_DIALOG, mActualScoreTV.text.toString())
    }

    override fun onEndDialogBtnClicked(view: View) {
        when (view.id) {
            R.id.ibtnHome -> {
                listener.backButtonPressedArcade()
                mEndGameDialog.arcadeDismissDialog()
            }
            R.id.ibtnRetry -> {
                listener.restartArcadeGame()
                mEndGameDialog.arcadeDismissDialog()
            }
            R.id.ibtnScoreBoard -> {
                mEndGameDialog.arcadeDismissDialog()
                listener.loadScoreBoardFromArcade()
            }
        }
    }

    override fun onDialogDismiss() {
        listener.arcadeModeHideNavBar()
    }

    override fun onPause() {
        super.onPause()
        mEndGameDialog.arcadeDismissDialog()
        listener.backButtonPressedArcade()
    }
}

