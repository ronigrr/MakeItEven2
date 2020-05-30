package com.example.makeiteven2

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.fragments.FragmentSettings
import com.example.makeiteven2.fragments.FragmentStartScreen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_start_screen.*

class MainActivity : AppCompatActivity(),FragmentStartScreen.StartScreenCallBack,FragmentSettings.SettingsFragmentCallBack{

    private val fragmentManager = supportFragmentManager
    private val fragmentStartScreen : FragmentStartScreen = FragmentStartScreen()
    private val fragmentSettings :FragmentSettings = FragmentSettings()
    private lateinit var appToolbar : Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appToolbar= toolBar as Toolbar
        setSupportActionBar(appToolbar)

        fragmentManager.beginTransaction().add(R.id.fragmentContainer,fragmentStartScreen,Constants.START_SCREEN_FRAGMENT_TAG).commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                //about
                val aboutDialog = Dialog(this)
                val dialogView: View =layoutInflater.inflate(R.layout.about_dialog, null)
                aboutDialog.setContentView(dialogView)
                aboutDialog.setCanceledOnTouchOutside(true)
                aboutDialog.show()
            }
            R.id.action_settings -> {
                //settings
                //TODO: Change and finish setting fragment
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragmentSettings,Constants.SETTINGS_SCREEN_FRAGMENT_TAG)
                    .addToBackStack(null).commit()
                appToolbar.visibility = View.GONE
                //item.isEnabled = false
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStartScreenButtonClicked(view: View) {
        when (view.id)
        {
            btnStageMode.id -> { Toast.makeText(this,"StageMode",Toast.LENGTH_SHORT).show()}
            btnArcadeMode.id ->Toast.makeText(this,"ArcadeMode",Toast.LENGTH_SHORT).show()
            btnScoreBoard.id->Toast.makeText(this,"ScoreBoard",Toast.LENGTH_SHORT).show()
            btnTutorial.id->Toast.makeText(this,"Tutorial",Toast.LENGTH_SHORT).show()
        }

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
