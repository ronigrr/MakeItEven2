package com.yoyoG.makeiteven2

import android.widget.ToggleButton
import com.yoyoG.makeiteven2.extras.EOperators
import com.yoyoG.makeiteven2.game.Game
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
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
        val game = Game(10)
        for (i in 0..1000)
            game.gameGenerator(mGameButtonsList)
    }
}
