package com.example.makeiteven2

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.fragments.FragmentSettings
import com.example.makeiteven2.fragments.FragmentLevelsScreen
import com.example.makeiteven2.fragments.FragmentStartScreen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_start_screen.*

class MainActivity : AppCompatActivity(),FragmentStartScreen.IFragmentsStartsScreenCallback
, FragmentLevelsScreen.IFragmentLevelsScreenCallback,FragmentSettings.SettingsFragmentCallBack {

    private val fragmentManager = supportFragmentManager
    private val fragmentStartScreen : FragmentStartScreen = FragmentStartScreen()
    private val fragmentSettings :FragmentSettings = FragmentSettings()
    private val fragmentLevelsScreen : FragmentLevelsScreen = FragmentLevelsScreen()
    private lateinit var appToolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appToolbar= toolBar as Toolbar
        appToolbar.title =" "
        setSupportActionBar(appToolbar)


        fragmentManager.beginTransaction().add(R.id.fragmentContainer,fragmentStartScreen,Constants.START_SCREEN_FRAGMENT_TAG).commit()

    }

    override fun onStartScreenFragmentButtonClicked(view: View) {
        when (view.id) {
            btnStageMode.id -> loadStageMode()
            btnArcadeMode.id -> Toast.makeText(this, "ArcadeMode", Toast.LENGTH_SHORT).show()
            btnScoreBoard.id -> Toast.makeText(this, "ScoreBoard", Toast.LENGTH_SHORT).show()
            btnTutorial.id -> Toast.makeText(this, "Tutorial", Toast.LENGTH_SHORT).show()
        }
    }
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.action_menu, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_about -> {
                    //about
                    val aboutDialog = Dialog(this)
                    val dialogView: View = layoutInflater.inflate(R.layout.about_dialog, null)
                    aboutDialog.setContentView(dialogView)
                    aboutDialog.setCanceledOnTouchOutside(true)
                    aboutDialog.show()
                }
                R.id.action_settings -> {
                    //settings
                    //TODO: Change and finish setting fragment
                    fragmentManager.beginTransaction().replace(
                        R.id.fragmentContainer,
                        fragmentSettings,
                        Constants.SETTINGS_SCREEN_FRAGMENT_TAG
                    )
                        .addToBackStack(null).commit()
                    appToolbar.visibility = View.GONE
                    //item.isEnabled = false
                }
                else -> {
                }
            }
            return super.onOptionsItemSelected(item)
        }

        private fun loadStageMode() {
            fragmentManager.beginTransaction().replace(
                R.id.fragmentContainer,
                fragmentLevelsScreen,
                Constants.LEVELS_SCREEN_FRAGMENT_TAG
            )
                .addToBackStack(null).commit()
        }

        override fun onLevelsFragmentLevelClicked(view: View) {
            //TODO: implement method
        }

        override fun onBackPressed() {
            super.onBackPressed()
            //TODO: Bug that reloads the recyclerview in levelsScreenFragment when you backpress and press stagemode again
        }

        override fun showToolBar() {
            appToolbar.visibility = View.VISIBLE
        }

        override fun onSeekBarMainVolume(mainVolume: Int) {
            TODO("Not yet implemented")
        }

        override fun onSeekBarSoundEffects(soundEffectsVolume: Int) {
            TODO("Not yet implemented")
        }

        override fun onResetGame() {
            TODO("Not yet implemented")
        }

        override fun onExitFromSettingsFragment() {
            onBackPressed()
        }
    }
