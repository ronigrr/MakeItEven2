package com.yoyoG.makeiteven2.game

import android.util.Log
import android.widget.ToggleButton
import com.yoyoG.makeiteven2.extras.Constants
import com.yoyoG.makeiteven2.extras.EOperators
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

open class Game(private var mDifficulty: Int ) {
    private var testSum = 0
    private var sum = 0
    private var mHint: String = ""
    private val mOperatorList = ArrayList<EOperators>()
    private val mBtnList = ArrayList<Int>()
    private var mMinInt = 0
    private var mMaxInt = 0
    private var mCurrentLevel = 0


    fun setDifficulty(difficulty: Int) {
        mDifficulty = difficulty
    }

    fun getHint(): String {
        return mHint
    }

    fun gameGenerator(playButtons: List<ToggleButton>): Int {
        Log.d("game", "GameGeneratorExecute")

        // for test mCurrentLevel = 8
        setMinAndMaxIntForGameGenerator()
        setCurrentLevel()
        val randomList = List(4) { Random.nextInt(mMinInt, mMaxInt) }
        //reset for new game generate
        mHint = ""
        mBtnList.clear()
        mOperatorList.clear()
        sum = 0
        testSum = 0

        val btn1: Int = randomList[0] + Random.nextInt(0, 4)
        val btn2: Int = randomList[1] + Random.nextInt(0, 4)
        val btn3: Int = randomList[2] + Random.nextInt(0, 4)
        val btn4: Int = randomList[3] + Random.nextInt(0, 4)

        mBtnList.add(btn1)
        mBtnList.add(btn2)
        mBtnList.add(btn3)
        mBtnList.add(btn4)


        var randIdx = Random.nextInt(0, 4)
        try {

            if (randIdx == 3) {
                if (btn1 % btn2 == 0) {
                    sum = btn1 / btn2
                    mHint = "$mHint((($btn1/$btn2)"
                    mOperatorList.add(EOperators.DIVIDE)
                } else {
                    sum = btn1 + btn2
                    mHint = "$mHint((($btn1+$btn2)"
                    mOperatorList.add(EOperators.PLUS)
                }
            } else if (randIdx == 2) {
                sum = btn1 * btn2
                mHint = "$mHint((($btn1*$btn2)"
                mOperatorList.add(EOperators.MULTIPLY)
            } else if (randIdx == 1) {
                if (btn1 - btn2 > 0) {
                    sum = btn1 - btn2
                    mHint = "$mHint((($btn1-$btn2)"
                    mOperatorList.add(EOperators.MINUS)
                } else {
                    sum = btn1 + btn2
                    mHint = "$mHint((($btn1+$btn2)"
                    mOperatorList.add(EOperators.PLUS)
                }
            } else {
                sum = btn1 + btn2
                mHint = "$mHint((($btn1+$btn2)"
                mOperatorList.add(EOperators.PLUS)
            }

            randIdx = Random.nextInt(0, 4)


            if (randIdx == 3) {
                if (sum % btn3 == 0) {
                    sum /= btn3
                    mHint = "$mHint/$btn3)"
                    mOperatorList.add(EOperators.DIVIDE)
                } else {
                    sum += btn3
                    mHint = "$mHint+$btn3)"
                    mOperatorList.add(EOperators.PLUS)
                }
            } else if (randIdx == 2) {
                sum *= btn3
                mHint = "$mHint*$btn3)"
                mOperatorList.add(EOperators.MULTIPLY)
            } else if (randIdx == 1) {
                if (sum - btn3 > 0) {
                    sum -= btn3
                    mHint = "$mHint-$btn3)"
                    mOperatorList.add(EOperators.MINUS)
                } else {
                    sum += btn3
                    mHint = "$mHint+$btn3)"
                    mOperatorList.add(EOperators.PLUS)
                }
            } else {
                sum += btn3
                mHint = "$mHint+$btn3)"
                mOperatorList.add(EOperators.PLUS)
            }
            randIdx = Random.nextInt(0, 4)

            if (randIdx == 3) {
                if (sum % btn4 == 0) {
                    sum /= btn4
                    mHint = "$mHint/$btn4)"
                    mOperatorList.add(EOperators.DIVIDE)
                } else {
                    sum += btn4
                    mHint = "$mHint+$btn4)"
                    mOperatorList.add(EOperators.PLUS)
                }
            } else if (randIdx == 2) {
                sum *= btn4
                mHint = "$mHint*$btn4)"
                mOperatorList.add(EOperators.MULTIPLY)
            } else if (randIdx == 1) {
                if (sum - btn4 > 0) {
                    sum -= btn4
                    mHint = "$mHint-$btn4)"
                    mOperatorList.add(EOperators.MINUS)
                } else {
                    sum += btn4
                    mHint = "$mHint+$btn4)"
                    mOperatorList.add(EOperators.PLUS)
                }
            } else {
                sum += btn4
                mHint = "$mHint+$btn4)"
                mOperatorList.add(EOperators.PLUS)
            }

            Collections.shuffle(playButtons)
            
            //set text to play buttons
            playButtons[0].textOff = btn1.toString()
            playButtons[1].textOff = btn2.toString()
            playButtons[2].textOff = btn3.toString()
            playButtons[3].textOff = btn4.toString()

            playButtons[0].textOn = btn1.toString()
            playButtons[1].textOn = btn2.toString()
            playButtons[2].textOn = btn3.toString()
            playButtons[3].textOn = btn4.toString()

            playButtons[0].text = btn1.toString()
            playButtons[1].text = btn2.toString()
            playButtons[2].text = btn3.toString()
            playButtons[3].text = btn4.toString()

            //test conditions for deliver the game
            if (isSumZero()) {
                Log.e("game", "sum is zero generate game again Execute gameGenerator")
                return gameGenerator(playButtons)
            }

            if (mDifficulty == 6) {
                if (isAllOperatorsTheSame()) {
                    Log.e("game", "mDifficulty == 6 | isAllOperatorsTheSame() Execute gameGenerator ")
                    return gameGenerator(playButtons)
                }

            } else if (mDifficulty == 7) {
                if (isAllOperatorsTheSame() || !isMultiplyExists()) {
                    Log.e("game", "mDifficulty == 7 | isAllOperatorsTheSame() !isMultiplyExists() Execute gameGenerator ")
                    return gameGenerator(playButtons)
                }
            } else if (mDifficulty == 8) {
                if (isAllOperatorsTheSame() || !isMultiplyExists()) {
                    Log.e("game", "mDifficulty == 8 | isAllOperatorsTheSame() !isMultiplyExists() Execute gameGenerator ")
                    return gameGenerator(playButtons)
                }
                if (mCurrentLevel%3 == 0 && !isDivideOperatorExits())
                {
                    Log.e("game", "mDifficulty == 8 | !isDivideOperatorExits() mCurrentLevel%3")
                    return gameGenerator(playButtons)
                }
                if (mCurrentLevel%2 == 0 && !isAllDifferentNumbers()){
                    Log.e("game", "mDifficulty == 8 | !isAllDifferentNumbers() mCurrentLevel%3")
                    return gameGenerator(playButtons)
                }

            }
            else if (mDifficulty == 9){
                if (isAllOperatorsTheSame() || !isMultiplyExists() || !isAllDifferentNumbers()) {
                    Log.e("game", "mDifficulty == 9 | isAllOperatorsTheSame() !isMultiplyExists() !isAllDifferentNumbers() Execute gameGenerator ")
                    return gameGenerator(playButtons)
                }

                if (mCurrentLevel%2 == 0 && !isDivideOperatorExits()){
                    Log.e("game", "mDifficulty == 9 | !isDivideOperatorExits() mCurrentLevel%2")
                    return gameGenerator(playButtons)
                }

                if (mCurrentLevel%4 == 0 && !isAllDifferentOperators()){
                    Log.e("game", "mDifficulty == 9 | !isAllDifferentOperators() mCurrentLevel%4")
                    return gameGenerator(playButtons)
                }

            }
            else {
                if (!isAllDifferentNumbers() || !isDivideOperatorExits() || !isMultiplyExists()
                    || isAllOperatorsTheSame() || !isAllDifferentOperators() )
                {
                    Log.e("game", "mDifficulty > 9 | !isAllDifferentNumbers() || !isDivideOperatorExits() || !isMultiplyExists() \n" +
                            "|| isAllOperatorsTheSame() || !isAllDifferentOperators()")
                    return gameGenerator(playButtons)
                }

            }
        } catch (ex: ArithmeticException) {
            Log.e("game", "divide by zero")
            return gameGenerator(playButtons)
        }
        try {
            testSum = 0
            for (i in 0..2) {
                if (i == 0) {
                    when (mOperatorList[i].operator) {
                        "+" -> testSum = mBtnList[i] + mBtnList[i + 1]
                        "-" -> testSum = mBtnList[i] - mBtnList[i + 1]
                        "*" -> testSum = mBtnList[i] * mBtnList[i + 1]
                        "/" -> testSum = mBtnList[i] / mBtnList[i + 1]
                    }
                } else {
                    when (mOperatorList[i].operator) {
                        "+" -> testSum += mBtnList[i + 1]
                        "-" -> testSum -= mBtnList[i + 1]
                        "*" -> testSum *= mBtnList[i + 1]
                        "/" -> testSum /= mBtnList[i + 1]
                    }
                }
            }
            if (testSum != sum) {

                throw Exception("unsolvable equation")
            }
        } catch (ex: Exception) {
            return gameGenerator(playButtons)
        }
        Log.e("calculation", "before return $mHint sum = $sum testsum = $testSum")
        return sum
    }

    private fun setCurrentLevel() {
        mCurrentLevel = Constants.User.currentLevel
    }

    private fun setMinAndMaxIntForGameGenerator() {
        val random = Random.nextInt(2)

        mMaxInt = mDifficulty

        if (mDifficulty >= 6) {
            mMinInt = 1
        }
        if ((mDifficulty == 7) && (random == 1)) {
            mMinInt = 2
        }
        if ((mDifficulty == 8) && (mCurrentLevel % 3 == 0) )
        {
            mMinInt = 2
            mMaxInt++
        }

        if (mDifficulty >=9){
            mMinInt = 2
            mMaxInt++
        }
    }

    private fun isAllDifferentOperators(): Boolean {
        var ret = false
        if (mOperatorList.distinct().size == 3) ret = true
        return ret
    }

    private fun isAllDifferentNumbers(): Boolean {
        var ret = false
        if (mBtnList.distinct().size == 4) ret = true
        return ret
    }

    private fun isDivideOperatorExits(): Boolean {
        var ret = false
        if (mOperatorList.contains(EOperators.DIVIDE)) ret = true
        return ret
    }

    private fun isAllOperatorsTheSame(): Boolean {
        var ret = false
        if (mOperatorList.distinct().size == 1) ret = true
        return ret

    }

    private fun isSumZero(): Boolean {
        var ret = false
        if (sum == 0) ret = true
        return ret
    }

    private fun isMultiplyExists(): Boolean {
        var ret = false
        if (mOperatorList.contains(EOperators.MULTIPLY)) ret = true
        return ret
    }

}