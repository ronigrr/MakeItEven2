package com.yoyoG.makeiteven2

import android.widget.ToggleButton
import com.yoyoG.makeiteven2.extras.EOperators
import com.yoyoG.makeiteven2.game.Game
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.random.Random

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@Suppress("UNUSED_CHANGED_VALUE")
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        for (i in 1..100)
        {val random = Random.nextInt(2)
            print(random)
        }


    }

    @Test
    fun printOperators() {
        print(EOperators.DIVIDE.operator)
        print(EOperators.PLUS.operator)
        print(EOperators.MINUS.operator)
        print(EOperators.MULTIPLY.operator)
    }

    @Test
    fun testgame() {
        val mGameButtonsList = ArrayList<ToggleButton>()
        var mTargetNumber : Int
        var retry : Int
        var averageOfRetry = 0.0
        val min = 200
        val max = 999
        var enterTime: Long
        val testStartTime: Long = System.currentTimeMillis()
        val testEndTime: Long
        var finishTime: Long
        var avgTime = 0.0
        val NUM_OF_GAMES_TO_TEST = 10000
        val DIFFICULTTY_TEST = 30


        var minTime: Long = 1
        var maxTime: Long = 0
        var minRetry = 1
        var maxRetry = 0


        val mGame = Game(DIFFICULTTY_TEST)

        for (i in 1..NUM_OF_GAMES_TO_TEST) {
            enterTime = System.currentTimeMillis()
            retry = -1
            do {
                mTargetNumber = mGame.gameGenerator(mGameButtonsList)

                retry++
                //print("game" + "num of retrys = ${retry++} difficulty = 8 min = $min max = $max\n")
            } while (mTargetNumber > max || mTargetNumber < min)
            finishTime = System.currentTimeMillis()
            val timeToGenerateGame = finishTime - enterTime

            if (timeToGenerateGame > maxTime) maxTime = timeToGenerateGame
            if (timeToGenerateGame < minTime) minTime = timeToGenerateGame


            averageOfRetry += retry
            if (retry < minRetry) minRetry = retry
            if (retry > maxRetry) maxRetry = retry
            avgTime += timeToGenerateGame

            print("game ${mGame.getHint()} = $mTargetNumber" + " num of retrys = ${retry++} difficulty = $DIFFICULTTY_TEST min = $min max = $max time = $timeToGenerateGame\n")

        }

        testEndTime = System.currentTimeMillis()
        print("""erage retrys ${averageOfRetry / NUM_OF_GAMES_TO_TEST}
avg time = ${avgTime / NUM_OF_GAMES_TO_TEST}
maxTime = $maxTime
minTime = $minTime
maxRetry = $maxRetry
minRetry = $minRetry
total time = ${testEndTime-testStartTime}""")


    }
}
