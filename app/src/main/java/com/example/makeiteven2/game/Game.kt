package com.example.makeiteven2.game

import android.util.Log
import android.widget.ToggleButton
import com.example.makeiteven2.extras.EOperators
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

open class Game(private var mDifficulty: Int) {
    private var mHint: String = ""

    fun setDifficulty(difficulty: Int) {
        mDifficulty = difficulty
    }

    fun getHint(): String {
        return mHint
    }

    fun gameGenerator(playButtons: List<ToggleButton>, minSum: Int, maxSum: Int): Int {
        val randomList = List(4) { Random.nextInt(mDifficulty) }
        val btnList = ArrayList<Int>()
        val operatorList = ArrayList<EOperators>()
        //reset hint for new game generate
        mHint = ""

        val btn1: Int = randomList[0] + Random.nextInt(0, 4)
        val btn2: Int = randomList[1] + Random.nextInt(0, 4)
        val btn3: Int = randomList[2] + Random.nextInt(0, 4)
        val btn4: Int = randomList[3] + Random.nextInt(0, 4)

        btnList.add(btn1)
        btnList.add(btn2)
        btnList.add(btn3)
        btnList.add(btn4)

        var sum: Int = -1

        var randIdx = Random.nextInt(0, 4)
        try {

            if (randIdx == 3) {
                if (btn1 % btn2 == 0) {
                    sum = btn1 / btn2
                    mHint = "$mHint((($btn1/$btn2)"
                    operatorList.add(EOperators.DIVIDE)
                } else {
                    sum = btn1 + btn2
                    mHint = "$mHint((($btn1+$btn2)"
                    operatorList.add(EOperators.PLUS)
                }
            } else if (randIdx == 2) {
                sum = btn1 * btn2
                mHint = "$mHint((($btn1*$btn2)"
                operatorList.add(EOperators.MULTIPLY)
            } else if (randIdx == 1) {
                if (btn1 - btn2 > 0) {
                    sum = btn1 - btn2
                    mHint = "$mHint((($btn1-$btn2)"
                    operatorList.add(EOperators.MINUS)
                } else {
                    sum = btn1 + btn2
                    mHint = "$mHint((($btn1+$btn2)"
                    operatorList.add(EOperators.PLUS)
                }
            } else {
                sum = btn1 + btn2
                mHint = "$mHint((($btn1+$btn2)"
                operatorList.add(EOperators.PLUS)
            }

            randIdx = Random.nextInt(0, 4)


            if (randIdx == 3) {
                if (sum % btn3 == 0) {
                    sum /= btn3
                    mHint = "$mHint/$btn3)"
                    operatorList.add(EOperators.DIVIDE)
                } else {
                    sum += btn3
                    mHint = "$mHint+$btn3)"
                    operatorList.add(EOperators.PLUS)
                }
            } else if (randIdx == 2) {
                sum *= btn3
                mHint = "$mHint*$btn3)"
                operatorList.add(EOperators.MULTIPLY)
            } else if (randIdx == 1) {
                if (sum - btn3 > 0) {
                    sum -= btn3
                    mHint = "$mHint-$btn3)"
                    operatorList.add(EOperators.MINUS)
                } else {
                    sum += btn3
                    mHint = "$mHint+$btn3)"
                    operatorList.add(EOperators.PLUS)
                }
            } else {
                sum += btn3
                mHint = "$mHint+$btn3)"
                operatorList.add(EOperators.PLUS)
            }
            randIdx = Random.nextInt(0, 4)

            if (randIdx == 3) {
                if (sum % btn4 == 0) {
                    sum /= btn4
                    mHint = "$mHint/$btn4)"
                    operatorList.add(EOperators.DIVIDE)
                } else {
                    sum += btn4
                    mHint = "$mHint+$btn4)"
                    operatorList.add(EOperators.PLUS)
                }
            } else if (randIdx == 2) {
                sum *= btn4
                mHint = "$mHint*$btn4)"
                operatorList.add(EOperators.MULTIPLY)
            } else if (randIdx == 1) {
                if (sum - btn4 > 0) {
                    sum -= btn4
                    mHint = "$mHint-$btn4)"
                    operatorList.add(EOperators.MINUS)
                } else {
                    sum += btn4
                    mHint = "$mHint+$btn4)"
                    operatorList.add(EOperators.PLUS)
                }
            } else {
                sum += btn4
                mHint = "$mHint+$btn4)"
                operatorList.add(EOperators.PLUS)
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

        } catch (ex: ArithmeticException) {
            Log.e("zero", "divide by zero")
            gameGenerator(playButtons, minSum, maxSum)
        }

        try {
            var testSum = 0
            for (i in 0..2) {
                if (i == 0) {
                    when (operatorList[i].operator) {
                        "+" -> testSum = btnList[i] + btnList[i + 1]
                        "-" -> testSum = btnList[i] - btnList[i + 1]
                        "*" -> testSum = btnList[i] * btnList[i + 1]
                        "/" -> testSum = btnList[i] / btnList[i + 1]
                    }
                } else {
                    when (operatorList[i].operator) {
                        "+" -> testSum += btnList[i + 1]
                        "-" -> testSum -= btnList[i + 1]
                        "*" -> testSum *= btnList[i + 1]
                        "/" -> testSum /= btnList[i + 1]
                    }
                }
            }
            //Test prints
            //print("$mHint \n")
            //print("$testSum $sum \n")

            if (testSum != sum) {
                throw Exception("unsolvable equation")
            }
        } catch (ex: Exception) {
            Log.e("calculation", "unsolvable equation")
            gameGenerator(playButtons, minSum, maxSum)
        }

        return sum
    }
}