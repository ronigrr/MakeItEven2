package com.example.makeiteven2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.fragments.FragmentStartScreen
import kotlinx.android.synthetic.main.fragment_start_screen.*

class MainActivity : AppCompatActivity(),FragmentStartScreen.OnButtonClicked {

    private val fragmentManager = supportFragmentManager
    private val fragmentStartScreen : FragmentStartScreen = FragmentStartScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager.beginTransaction().add(R.id.fragmentContainer,fragmentStartScreen,Constants.START_SCREEN_FRAGMENT_TAG).commit()

    }

    override fun onButtonClicked(view: View) {
        when (view.id)
        {
            btnStageMode.id -> Toast.makeText(this,"StageMode",Toast.LENGTH_SHORT).show()
            btnArcadeMode.id ->Toast.makeText(this,"ArcadeMode",Toast.LENGTH_SHORT).show()
            btnScoreBoard.id->Toast.makeText(this,"ScoreBoard",Toast.LENGTH_SHORT).show()
            btnTutorial.id->Toast.makeText(this,"Tutorial",Toast.LENGTH_SHORT).show()
        }

    }
}
