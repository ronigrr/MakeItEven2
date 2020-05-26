package com.example.makeiteven2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.fragments.FragmentStartScreen

class MainActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private val fragmentStartScreen : FragmentStartScreen = FragmentStartScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager.beginTransaction().add(R.id.fragmentContainer,fragmentStartScreen,Constants.START_SCREEN_FRAGMENT_TAG).commit()

    }
}
