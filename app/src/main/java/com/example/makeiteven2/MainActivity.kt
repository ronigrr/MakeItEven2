package com.example.makeiteven2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.fragments.FragmentLevelsScreen
import com.example.makeiteven2.fragments.FragmentStartScreen
import kotlinx.android.synthetic.main.fragment_start_screen.*

class MainActivity : AppCompatActivity(),FragmentStartScreen.IFragmentsStartsScreenCallback , FragmentLevelsScreen.IFragmentLevelsScreenCallback {

    private val fragmentManager = supportFragmentManager
    private val fragmentStartScreen : FragmentStartScreen = FragmentStartScreen()
    private val fragmentLevelsScreen : FragmentLevelsScreen = FragmentLevelsScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager.beginTransaction().add(R.id.fragmentContainer,fragmentStartScreen,Constants.START_SCREEN_FRAGMENT_TAG).commit()

    }

    override fun onStartScreenFragmentButtonClicked(view: View) {
        when (view.id)
        {
            btnStageMode.id -> loadStageMode()
            btnArcadeMode.id ->Toast.makeText(this,"ArcadeMode",Toast.LENGTH_SHORT).show()
            btnScoreBoard.id->Toast.makeText(this,"ScoreBoard",Toast.LENGTH_SHORT).show()
            btnTutorial.id->Toast.makeText(this,"Tutorial",Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadStageMode() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragmentLevelsScreen,Constants.LEVELS_SCREEN_FRAGMENT_TAG)
            .addToBackStack(null).commit()
    }

    override fun onLevelsFragmentLevelClicked(view: View) {
        //TODO: implement method
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //TODO: Bug that reloads the recyclerview in levelsScreenFragment when you backpress and press stagemode again
    }
}
