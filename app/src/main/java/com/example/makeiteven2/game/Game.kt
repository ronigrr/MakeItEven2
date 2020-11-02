package com.example.makeiteven2.game

import android.util.Log
import android.widget.ToggleButton
import com.example.makeiteven2.exception.GameGeneratorException
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.extras.EOperators
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

open class Game(private var mDifficulty: Int) {
    private var testSum = 0
    private var sum = 0
    private var mHint: String = ""
    private val mOperatorList = ArrayList<EOperators>()
    private val mBtnList = ArrayList<Int>()
    fun setDifficulty(difficulty: Int) {
        mDifficulty = difficulty
    }

    fun getHint(): String {
        return mHint
    }

    fun gameGenerator(playButtons: List<ToggleButton>): Int {

        val randomList = List(4) { Random.nextInt(mDifficulty) }


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
            if (sum == 0) {
                Log.e("game", "sum is zero generate game again")
                throw GameGeneratorException(Constants.SUM_ZERO_EX)
            }
            if (isSameOperators() && mDifficulty > 8) {
                Log.e("game", "sum is zero generate game again")
                throw GameGeneratorException(Constants.SAME_OPERATOR_EX)
            }
            if (isZeroButton() && mDifficulty > 8) {
                throw GameGeneratorException(Constants.ZERO_BUTTON_EX)
            }

        } catch (ex: ArithmeticException) {
            Log.e("game", "divide by zero")
            return gameGenerator(playButtons)
        } catch (ex: GameGeneratorException) {
            when (ex.message) {
                Constants.SUM_ZERO_EX -> {
                    return gameGenerator(playButtons)
                }
                Constants.SAME_OPERATOR_EX -> {
                    return gameGenerator(playButtons)
                }
            }
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
            //Test prints
            print("$mHint \n")
            print("$testSum $sum \n")

            if (testSum != sum) {

                throw Exception("unsolvable equation")
            }
        } catch (ex: Exception) {
            return gameGenerator(playButtons)
        }
        Log.e("calculation", "before return $mHint sum = $sum testsum = $testSum")
        return sum
    }

    private fun isZeroButton(): Boolean {
        return mBtnList.contains(0)
    }

    private fun isSameOperators(): Boolean {
        var ret = false
        if (mOperatorList.distinct().size == 1) ret = true
        return ret

    }
}