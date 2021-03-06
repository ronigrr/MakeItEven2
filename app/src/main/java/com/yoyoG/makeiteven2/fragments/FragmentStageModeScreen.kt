package com.yoyoG.makeiteven2.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup
import com.yoyoG.makeiteven2.R
import com.yoyoG.makeiteven2.data_models.StageInfo
import com.yoyoG.makeiteven2.dialogs.DialogEndGameManager
import com.yoyoG.makeiteven2.dialogs.DialogStore
import com.yoyoG.makeiteven2.extras.Constants
import com.yoyoG.makeiteven2.game.GameFactory
import com.yoyoG.makeiteven2.intefaces.IEndDialogBtnClickedListener
import com.yoyoG.makeiteven2.intefaces.IFragmentStageModeListener
import com.yoyoG.makeiteven2.intefaces.IStoreDialogBtnClickedListener
import com.yoyoG.makeiteven2.managers.AnimationsManager
import com.yoyoG.makeiteven2.managers.AudioManager
import com.yoyoG.makeiteven2.managers.ShearedPrefManager
import com.yoyoG.makeiteven2.room.DatabaseHelper
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_game_stage.view.*
import me.toptas.fancyshowcase.FancyShowCaseQueue
import me.toptas.fancyshowcase.FancyShowCaseView


class FragmentStageModeScreen(levelNumber: Int) : Fragment(), View.OnClickListener, IEndDialogBtnClickedListener, IStoreDialogBtnClickedListener {

    private lateinit var mStoreIBTN: ImageButton
    private lateinit var mCoinsIV: ImageView
    private lateinit var mLevelNumberTV: TextView
    private lateinit var mCoinsLeftTV: TextView
    private lateinit var mTargetNumberTV: TextView
    private lateinit var mBackButtonIB: ImageButton
    private lateinit var mRetryButtonIB: ImageButton
    private lateinit var mHintIB: ImageButton
    private lateinit var mSosHintIB: ImageButton

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

    private lateinit var mEndGameDialog: DialogEndGameManager
    private lateinit var mStoreDialog: DialogStore

    private val mGameButtonsList = ArrayList<ToggleButton>()
    private val mOperatorsList = ArrayList<ToggleButton>()

    private lateinit var rootView: View

    private var mFullHintString: String = ""
    private var mHalfHintString: String = ""
    private var mNumberOfCoinsLeft = Constants.User.coinsLeft
    private var mLevelNum = levelNumber
    private var mTargetNumber = 0
    private val mGame = GameFactory.getGame(Constants.STAGE_GAME_TYPE, 12)
    private var mStageInfoArray: ArrayList<StageInfo> = Constants.User.stageList


    private lateinit var listener: IFragmentStageModeListener

    private var isNumberSelected = false
    private var isOperatorSelected = false
    private var num1 = Int.MAX_VALUE
    private var num2 = Int.MAX_VALUE
    private var selectedNumberID1 = 0
    private var selectedNumberID2 = 0
    private var operator = ""
    private var selectedOperatorID = 0

    private lateinit var fancyShowCaseQueue: FancyShowCaseQueue

    private lateinit var sosToasty: Toast
    private lateinit var hintToasty: Toast
    private var retry: Int = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentStageModeListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + "must implement IFragmentStageModeListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_game_stage, container, false)
        gameSetup()
        gameInit()
        initDialogs()
        Handler(Looper.getMainLooper()).postDelayed({
            startTutorial()
        }, 200)
        return rootView
    }

    private fun initCoinsLiveData() {
        Constants.liveDataCoins.observe(this.activity!!, {
            mCoinsLeftTV.text = it.toString()
            mNumberOfCoinsLeft = it
        })

    }

    private fun initDialogs() {
        mEndGameDialog = DialogEndGameManager(this, context!!)
        mStoreDialog = DialogStore(this, context!!, this.activity!!)
    }

    private fun startTutorial() {
        fancyShowCaseQueue = FancyShowCaseQueue()

        if (arguments?.getBoolean(Constants.IS_TUTORIAL) != null || ShearedPrefManager.getIsFirstTimeInStageMode(context!!)) {

            if (ShearedPrefManager.getIsFirstTimeInStageMode(context!!)) {
                ShearedPrefManager.setIsFirstTimeInStageMode(context!!, false)
            }
            val one = FancyShowCaseView.Builder(activity!!)
                .focusOn(rootView.theTargetNumberTV)
                .title("Reach this number in order to complete the stage")
                .build()
            val two = FancyShowCaseView.Builder(activity!!)
                .focusOn(rootView.group_choices_of_numbers)
                .title("You need to use ALL four numbers to do so")
                .titleGravity(Gravity.BOTTOM)
                .build()
            val three = FancyShowCaseView.Builder(activity!!)
                .focusOn(rootView.group_choices_of_operators)
                .title("Use all operators as much as you want")
                .titleGravity(Gravity.TOP)
                .build()
            val four = FancyShowCaseView.Builder(activity!!)
                .focusOn(rootView.hintButtonIB)
                .title("Need a hint? click here for one, notice you get only one for each stage you complete")
                .build()
            val five = FancyShowCaseView.Builder(activity!!)
                .focusOn(rootView.sosButtonIB)
                .title("You want the full answer? click here but be careful it's expensive")
                .titleGravity(Gravity.BOTTOM)
                .build()
            val six = FancyShowCaseView.Builder(activity!!)
                .focusOn(rootView.coinsLeftTV)
                .title("This is the numbers of coins left,hint costs one coin and sos costs 3")
                .titleGravity(Gravity.BOTTOM)
                .build()
            val gl = FancyShowCaseView.Builder(activity!!)
                .title("Good Luck!")
                .build()
            fancyShowCaseQueue.add(one).add(two).add(three).add(four).add(five).add(six).add(gl).show()
        }
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
        setButtonsAnimation()
        initCoinsLiveData()
    }

    private fun setButtonsAnimation() {
        mBackButtonIB.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mCoinsLeftTV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        for (i in 0..3) {
            mGameButtonsList[i].startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
            mOperatorsList[i].startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        }
        mStoreIBTN.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mCoinsIV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mCoinsLeftTV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mHintIB.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mSosHintIB.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mRetryButtonIB.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mLevelNumberTV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
        mTargetNumberTV.startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
    }

    private fun initToasty() {
        Toasty.Config.getInstance().tintIcon(false).setTextSize(30).allowQueue(true).apply()
        hintToasty = Toasty.info(context!!, mHalfHintString, Toast.LENGTH_SHORT, true)
        sosToasty = Toasty.info(context!!, mFullHintString, Toast.LENGTH_LONG, true)
    }

    private fun setButtonsListeners() {
        val hintListener = View.OnClickListener { _ ->
            context?.let { _ ->
                hintToasty.cancel()
                Handler(Looper.getMainLooper()).postDelayed({
                    hintToasty.show()
                }, 50)
            }
            mNumberOfCoinsLeft-= Constants.HINT_COST
            DatabaseHelper.saveCoinsToDataBase(context!!.applicationContext, mNumberOfCoinsLeft)
            checkIfNeedToShowSosHint()
        }

        val sosHintListener = View.OnClickListener { _ ->
            context?.let { _ ->
                sosToasty.cancel()
                Handler(Looper.getMainLooper()).postDelayed({
                    sosToasty.show()
                }, 50)
            }
            mNumberOfCoinsLeft -= Constants.SOS_COST
            DatabaseHelper.saveCoinsToDataBase(context!!.applicationContext, mNumberOfCoinsLeft)
            checkIfNeedToShowSosHint()
        }
        mStoreIBTN.apply {
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
            setOnClickListener {
                mStoreDialog.showStoreDialog()
            }
        }
        mHintIB.apply {
            setOnClickListener(hintListener)
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        }

        mSosHintIB.apply {
            setOnClickListener(sosHintListener)
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        }
        mBackButtonIB.apply {
            setOnClickListener { listener.backButtonPressedStage() }
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        }
        mRetryButtonIB.apply {
            setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
            setOnClickListener {
                startAnimation(AnimationsManager.getInstance(context!!).getRotateAnimation())
                gameInit()
                for (tb in mGameButtonsList) {
                    tb.startAnimation(AnimationsManager.getInstance(context!!).getBounceAndShakeAnimation())
                }
            }
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
                setOnClickListener(this@FragmentStageModeScreen)
                setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
                isEnabled = false
            }
        }
        for (tb in mOperatorsList) {
            tb.apply {
                setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
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
                startAnimation(AnimationsManager.getInstance(context!!).getBounceAnimation())
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
        val currentStage = Constants.User.currentLevel
        mStageInfoArray = Constants.User.stageList
        if (mStageInfoArray.size < mLevelNum || currentStage < mLevelNum) {
            when (mLevelNum) {
                in 1..10 -> {
                    min = 0
                    max = 10
                    difficulty = 5
                }
                in 11..20 -> {
                    min = 10
                    max = 20
                    difficulty = 6
                }
                in 21..40 -> {
                    min = 20
                    max = 40
                    difficulty = 7
                }
                in 41..60 -> {
                    min = 40
                    max = 60
                    difficulty = 8
                }
                in 61..100 -> {
                    min = 60
                    max = 100
                    difficulty = 9
                }
                in 101..150 -> {
                    min = 80
                    max = 120
                    difficulty = 13
                }
                in 151..200 -> {
                    min = 130
                    max = 170
                    difficulty = 20
                }
                in 201..250 -> {
                    min = 170
                    max = 220
                    difficulty = 24
                }
                else -> {
                    min = 200
                    max = 999
                    difficulty = 30
                }
            }
            mGame?.setDifficulty(difficulty)
            do {
                if (mGame != null) {
                    mTargetNumber = mGame.gameGenerator(mGameButtonsList)
                    mFullHintString = mGame.getHint()
                    mHalfHintString = mFullHintString.substringBefore(")") + ")"

                    Log.e("game", "num of retrys = ${retry++} difficulty = $difficulty min = $min max = $max")
                }
            } while (mTargetNumber > max || mTargetNumber < min)

            retry = 0
            mTargetNumberTV.text = mTargetNumber.toString()
            val stageInfo = StageInfo(
                mGameButtonsList[0].text.toString().toInt(),
                mGameButtonsList[1].text.toString().toInt(),
                mGameButtonsList[2].text.toString().toInt(),
                mGameButtonsList[3].text.toString().toInt(),
                mTargetNumber,
                mFullHintString
            )
            DatabaseHelper.saveStageInfo(context!!.applicationContext, stageInfo)

        } else {
            startSavedGameInfo()
        }
        initToasty()
    }

    override fun onPause() {
        super.onPause()
//        mStoreDialog.hideStoreDialog()
        sosToasty.cancel()
        hintToasty.cancel()
    }

    private fun startSavedGameInfo() {

        mGameButtonsList.shuffle()

        mGameButtonsList[0].text = mStageInfoArray[mLevelNum - 1].num1.toString()
        mGameButtonsList[0].textOn = mStageInfoArray[mLevelNum - 1].num1.toString()
        mGameButtonsList[0].textOff = mStageInfoArray[mLevelNum - 1].num1.toString()

        mGameButtonsList[1].text = mStageInfoArray[mLevelNum - 1].num2.toString()
        mGameButtonsList[1].textOn = mStageInfoArray[mLevelNum - 1].num2.toString()
        mGameButtonsList[1].textOff = mStageInfoArray[mLevelNum - 1].num2.toString()

        mGameButtonsList[2].text = mStageInfoArray[mLevelNum - 1].num3.toString()
        mGameButtonsList[2].textOn = mStageInfoArray[mLevelNum - 1].num3.toString()
        mGameButtonsList[2].textOff = mStageInfoArray[mLevelNum - 1].num3.toString()

        mGameButtonsList[3].text = mStageInfoArray[mLevelNum - 1].num4.toString()
        mGameButtonsList[3].textOn = mStageInfoArray[mLevelNum - 1].num4.toString()
        mGameButtonsList[3].textOff = mStageInfoArray[mLevelNum - 1].num4.toString()

        mTargetNumber = mStageInfoArray[mLevelNum - 1].target
        mTargetNumberTV.text = mTargetNumber.toString()
        mFullHintString = mStageInfoArray[mLevelNum - 1].hint
        mHalfHintString = mFullHintString.substringBefore(")") + ")"
    }

    private fun initFragmentMembersFromView() {
        mLevelNumberTV = rootView.levelTV // "Level: X"
        mCoinsLeftTV = rootView.coinsLeftTV //"Hints: X"
        mCoinsIV = rootView.coinsIV
        mTargetNumberTV = rootView.theTargetNumberTV
        mBackButtonIB = rootView.backButtonIB
        mRetryButtonIB = rootView.restartLevelIB
        mHintIB = rootView.hintButtonIB
        mSosHintIB = rootView.sosButtonIB
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


        mNumberOfCoinsLeft = Constants.User.coinsLeft
        checkIfNeedToShowSosHint()

        val textToShow = resources.getText(R.string.level_number).toString() + " " + mLevelNum.toString()
        mLevelNumberTV.text = textToShow
        mStoreIBTN = rootView.IBtnStoreStageMode
    }

    override fun onClick(v: View?) {
        if (!(v as ToggleButton).isChecked) {
            context?.let { AudioManager.getInstance(it).playBtnOn() }
        } else if (v.isChecked) {
            context?.let { AudioManager.getInstance(it).playBtnOff() }
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
//        Toast.makeText(this, "" + num1, Toast.LENGTH_SHORT).show();
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
                mEndGameDialog.showEndDialog(Constants.LOSE_DIALOG)
                context?.let { AudioManager.getInstance(it).playWaWaSound() }
                gameInit()
                return
            }

            if (i == 1) {
                //game finished

                if (mTargetNumber == sum) {
                    //you win
                    var currentStage = Constants.User.currentLevel
                    if (mLevelNum == currentStage) {
                        currentStage++
                        //add coins on every odd level
                        if (currentStage % 2 == 0 ){
                        DatabaseHelper.addCoins(context!!, 1)
                        checkIfNeedToShowSosHint()
                        }
                    }
                    DatabaseHelper.saveCurrentStage(context!!.applicationContext, currentStage)
                    Handler(Looper.getMainLooper()).postDelayed({
                        mEndGameDialog.showEndDialog(Constants.WIN_DIALOG)
                        AnimationsManager.getInstance(context!!).getConfetti(rootView.main_constraint)
                    }, 200)
                    context?.let { AudioManager.getInstance(it).playTaDaSound() }
                } else {
                    //you loose
                    context?.let { AudioManager.getInstance(it).playWaWaSound() }
                    Handler(Looper.getMainLooper()).postDelayed({
                        mEndGameDialog.showEndDialog(Constants.LOSE_DIALOG)
                    }, 200)
                    gameInit()
                }
            }

        }
    }

    private fun checkIfNeedToShowSosHint() {
        if (mNumberOfCoinsLeft >= Constants.HINT_COST) {
            mHintIB.isEnabled = true
            mHintIB.setImageResource(R.drawable.ic_help)
        } else {
            mHintIB.isEnabled = false
            mHintIB.setImageResource(R.drawable.ic_help_off)
        }
        if (mNumberOfCoinsLeft >= Constants.SOS_COST) {
            mSosHintIB.isEnabled = true
            mSosHintIB.setImageResource(R.drawable.ic_sos)
        } else {
            mSosHintIB.isEnabled = false
            mSosHintIB.setImageResource(R.drawable.ic_sosoff)
        }
    }

    override fun onEndDialogBtnClicked(view: View) {
        when (view.id) {
            R.id.ibtnHome -> {
                listener.backButtonPressedStage()
                mEndGameDialog.dismissDialogWithAd(mLevelNum)
            }
            R.id.ibtnRetry -> {
                gameInit()
                mEndGameDialog.dismissDialogWithAd(mLevelNum)
            }
            R.id.ibtnNext -> {
                mLevelNumberTV.text = context!!.resources.getText(R.string.level_number).toString() + " " + (++mLevelNum).toString()
                gameInit()
                mEndGameDialog.dismissDialogWithAd(mLevelNum)
            }
        }
    }

    override fun onDialogDismiss() {
        listener.onStageModeHideNavBar()
    }

    override fun onDetach() {
        super.onDetach()
        fancyShowCaseQueue.cancel(true)
    }

    override fun onStoreDialogBtnClicked(view: View) {
    }

    override fun storeDialogDismissed() {
        listener.onStageModeHideNavBar()
        checkIfNeedToShowSosHint()
    }

    override fun onResume() {
        super.onResume()
        context?.let { AudioManager.getInstance(it).playLoopMusicForSpecificFragment(Constants.STAGE_MODE_SCREEN_FRAGMENT_TAG) }
    }
}



