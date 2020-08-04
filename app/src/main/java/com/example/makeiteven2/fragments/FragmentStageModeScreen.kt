package com.example.makeiteven2.fragments

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.example.makeiteven2.R
import com.example.makeiteven2.data_models.StageInfo
import com.example.makeiteven2.extras.Animations
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.game.GameFactory
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_game_stage.view.*

class FragmentStageModeScreen : Fragment(), View.OnClickListener {

    private lateinit var mStageInfoArray: ArrayList<StageInfo>
    private lateinit var mLevelNumberTV: TextView
    private lateinit var mHintsLeftTV: TextView
    private lateinit var mTargetNumberTV: TextView
    private lateinit var mBackButtonIB: ImageButton
    private lateinit var mRetryButtonIB: ImageButton
    private lateinit var mHintIB: ImageButton

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

    private var mHintString: String = ""
    private var mNumberHintsLeft = 3
    private var mLevelNum = 1 //should get from init fragment
    private var mTargetNumber = 0 //should get from save
    private val mGame = GameFactory.getGame(Constants.STAGE_GAME_TYPE, 12)

    private lateinit var listener: IFragmentStageModeListener

    private var isNumberSelected = false
    private var isOperatorSelected = false
    private var num1 = 0
    private var num2 = 0
    private var selectedNumberID1 = 0
    private var selectedNumberID2 = 0
    private var operator = ""
    private var selectedOperatorID = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentStageModeListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + "must implement IFragmentStageModeListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_game_stage, container, false)
        initToasty()
        gameSetup(rootView)
        gameInit()


        return rootView
    }

    private fun gameSetup(view: View) {
        initFragmentMembersFromView(view)
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
            context?.let { letContext ->
                Toasty.info(letContext, mHintString, Toast.LENGTH_SHORT, true).show()
            }
            mNumberHintsLeft--
            //TODO:save updated num of hints
            val textToShow = "${resources.getText(R.string.hints_left)}" + " $mNumberHintsLeft"
            mHintsLeftTV.text = textToShow
            if (mNumberHintsLeft == 0) {
                (view as ImageButton).setImageResource(R.drawable.ic_help_off)
                view.isEnabled = false
            }
        }
        mHintIB.apply {
            setOnClickListener(hintListener)
            setOnTouchListener(Animations.getTouchAnimation(context))
        }
        mBackButtonIB.apply {
            setOnClickListener { listener.backButtonPressed() }
            setOnTouchListener(Animations.getTouchAnimation(context))
        }
        mRetryButtonIB.apply {
            setOnTouchListener(Animations.getTouchAnimation(context))
            setOnClickListener {
                startAnimation(Animations.getRotateAnimation(context))
                gameInit()
                for (tb in mGameButtonsList) {
                    tb.startAnimation(Animations.getBounceAndShakeAnimation(context))
                }
            }
        }
        mNumberGroup.apply {
            setOnCheckedChangeListener { group, checkedId ->
                val checkedTB = findViewById<ToggleButton>(checkedId)
                if (isNumberSelected && isOperatorSelected) {
                    num2 = checkedTB.text.toString().toInt()
                    selectedNumberID2 = checkedId
                } else {
                    num1 = checkedTB.text.toString().toInt()
                    selectedNumberID1 = checkedId
                }
            }
        }
        mOperatorGroup.apply {
            setOnCheckedChangeListener { group, checkedId ->
                val checkedTB = findViewById<ToggleButton>(checkedId)
                operator = checkedTB.tag.toString()
                isOperatorSelected = true
                selectedOperatorID = checkedId
            }
        }
        for (tb in mGameButtonsList) {
            tb.apply {
                setOnClickListener(this@FragmentStageModeScreen)
                setOnTouchListener(Animations.getTouchAnimation(context))
                isEnabled = false
            }
        }
        for (tb in mOperatorsList) {
            tb.apply {
                setOnTouchListener(Animations.getTouchAnimation(context))
                setOnClickListener(this@FragmentStageModeScreen)
            }
        }
    }

    private fun gameInit() {
        for (tb in mGameButtonsList) {
            tb.apply {
                visibility = View.VISIBLE
                isEnabled = true
                isChecked = false
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
        var difficulty = 6
        val currentStage = 1 //todo: get from datastore
        mStageInfoArray = ArrayList<StageInfo>()
        if (mStageInfoArray.size < mLevelNum || currentStage < mLevelNum) {
            when (mLevelNum) {
                in 0..10 -> {
                    min = 0
                    max = 20
                    difficulty = 6
                }
                in 11..20 -> {
                    min = 20
                    max = 40
                    difficulty = 8
                }
                in 21..30 -> {
                    min = 40
                    max = 60
                    difficulty = 9
                }
                in 31..40 -> {
                    min = 60
                    max = 90
                    difficulty = 10
                }
                else -> {
                    min = 80
                    max = 120
                    difficulty = 12
                }
            }
            mGame?.setDifficulty(difficulty)
            do {
                if (mGame != null) {
                    mTargetNumber = mGame.gameGenerator(mGameButtonsList, min, max)
                }
            } while (mTargetNumber > max || mTargetNumber < min)

            mTargetNumberTV.text = mTargetNumber.toString()
            val stageInfo = StageInfo(
                    mGameButtonsList[0].text.toString().toInt(),
                    mGameButtonsList[1].text.toString().toInt(),
                    mGameButtonsList[2].text.toString().toInt(),
                    mGameButtonsList[3].text.toString().toInt(),
                    mTargetNumber,
                    mHintString)
            //Todo: save stage info

        }else{
            startSavedGameInfo()
        }

    }

    private fun startSavedGameInfo() {

        mGameButtonsList.shuffle()

        mGameButtonsList[0].textOff = mStageInfoArray[mLevelNum-1].num1.toString()
        mGameButtonsList[0].textOff = mStageInfoArray[mLevelNum-1].num1.toString()
        mGameButtonsList[0].textOff = mStageInfoArray[mLevelNum-1].num1.toString()

        mGameButtonsList[1].textOff = mStageInfoArray[mLevelNum-1].num2.toString()
        mGameButtonsList[1].textOff = mStageInfoArray[mLevelNum-1].num2.toString()
        mGameButtonsList[1].textOff = mStageInfoArray[mLevelNum-1].num2.toString()

        mGameButtonsList[2].textOff = mStageInfoArray[mLevelNum-1].num3.toString()
        mGameButtonsList[2].textOff = mStageInfoArray[mLevelNum-1].num3.toString()
        mGameButtonsList[2].textOff = mStageInfoArray[mLevelNum-1].num3.toString()

        mGameButtonsList[3].textOff = mStageInfoArray[mLevelNum-1].num4.toString()
        mGameButtonsList[3].textOff = mStageInfoArray[mLevelNum-1].num4.toString()
        mGameButtonsList[3].textOff = mStageInfoArray[mLevelNum-1].num4.toString()

        mTargetNumber = mStageInfoArray[mLevelNum-1].target
        mTargetNumberTV.text = mTargetNumber.toString()
        mHintString = mStageInfoArray[mLevelNum-1].hint
    }


    private fun initFragmentMembersFromView(rootView: View) {
        mLevelNumberTV = rootView.levelTV // "Level: X"
        mHintsLeftTV = rootView.hintsLeftTV //"Hints: X"
        mTargetNumberTV = rootView.theTargetNumberTV
        mBackButtonIB = rootView.backButtonIB
        mRetryButtonIB = rootView.restartLevelIB
        mHintIB = rootView.hintButton3IB
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


        mNumberHintsLeft//TODO:get from memo
        if (mNumberHintsLeft == 0) {
            mHintIB.isEnabled = false
            mHintIB.setImageResource(R.drawable.ic_help_off)
        }
        mLevelNum //TODO: get from memo
        var textToShow = resources.getText(R.string.level_number).toString() + mLevelNum.toString()
        mLevelNumberTV.text = textToShow
        textToShow = resources.getString(R.string.hints_left) + " $mNumberHintsLeft"
        mHintsLeftTV.text = textToShow

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onClick(v: View?) {
//        Handler().post {
//            val btn_off: MediaPlayer
//            val btn_On: MediaPlayer
//            btn_off = MediaPlayer.create(this@GameActivity, R.raw.btn_off_sound)
//            btn_On = MediaPlayer.create(this@GameActivity, R.raw.btn_on_sound)
//            btn_On.setVolume(sound_Effects_Volume, sound_Effects_Volume)
//            btn_off.setVolume(sound_Effects_Volume, sound_Effects_Volume)
//            if (!(v as ToggleButton).isChecked) {
//                btn_On.start()
//            } else if ((v as ToggleButton).isChecked) {
//                btn_off.start()
//            }
//        }


    }
}

interface IFragmentStageModeListener {
    fun backButtonPressed()
}

