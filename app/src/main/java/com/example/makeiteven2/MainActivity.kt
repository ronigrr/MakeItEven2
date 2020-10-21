package com.example.makeiteven2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.example.makeiteven2.adapters.LevelsAdapter
import com.example.makeiteven2.data_models.StageInfo
import com.example.makeiteven2.extras.AudioManager
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.extras.GoogleAddManager
import com.example.makeiteven2.fragments.*
import com.example.makeiteven2.intefaces.*
import com.example.makeiteven2.room.DatabaseHelper
import com.example.makeiteven2.room.RoomUserNote
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_start_screen.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), IFragmentsStartsScreenListener, IFragmentSettingsListener, LevelsAdapter.ILevelsAdapter,
    FragmentDialogNickName.DialogListener, IFragmentStageModeListener, IFragmentArcadeModeListener, IFragmentLevelsScreenListener,
    IFragmentScoreBoardScreenListener {

    private val fragmentManager = supportFragmentManager

    private lateinit var mSharedPref: SharedPreferences
    private lateinit var mEditor: Editor

    private lateinit var appToolbar: Toolbar

    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var appUpdateInfoTask: Task<AppUpdateInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init3DotToolBar()
        initUpdateManager()
        Thread { GoogleAddManager.loadRewardAD(this) }.run()
        mSharedPref = applicationContext.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
        mEditor = mSharedPref.edit()
        startLoadingApp()
    }

    private fun startLoadingApp() {
        if (mSharedPref.getBoolean(Constants.IS_FIRST_TIME_IN_APP, FALSE) == FALSE) {
            Handler(Looper.getMainLooper()).postDelayed({
                setMainActivityVisible()
                firstTimeInApp()
            }, 5000)
        } else {
            loadUser()
            Handler(Looper.getMainLooper()).postDelayed({
                loadStartScreen()
            }, 5000)
        }
    }

    private fun initUpdateManager() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateInfoTask = appUpdateManager.appUpdateInfo
    }

    private fun setMainActivityVisible() {
        fragmentContainer.visibility = View.VISIBLE
        show3DotsToolBar()
    }

    private fun init3DotToolBar() {
        appToolbar = toolBar as Toolbar
        appToolbar.title = " "
        setSupportActionBar(appToolbar)
    }

    private fun loadUser() {
        GlobalScope.launch {
            DatabaseHelper.loadUserToConstants(applicationContext)
            AudioManager.startGameMusic(this@MainActivity)
        }
    }

    private fun loadStartScreen() {
        setMainActivityVisible()
        fragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, FragmentStartScreen(), Constants.START_SCREEN_FRAGMENT_TAG)
            .commit()
    }

    override fun onStartScreenFragmentButtonClicked(view: View) {
        when (view.id) {
            btnStageMode.id -> loadLevelScreen()
            btnArcadeMode.id -> loadArcadeMode()
            btnScoreBoard.id -> loadScoreBoard()
            btnTutorial.id -> loadStageModeWithTutorial()
        }
    }

    private fun loadScoreBoard() {
        fragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            FragmentScoreBoard(),
            Constants.SCOREBOARD_SCREEN_FRAGMENT_TAG
        )
            .addToBackStack(null).commit()
    }

    private fun loadStageModeWithTutorial() {
        hide3DotsToolBar()
        val arguments = bundleOf(Constants.IS_TUTORIAL to Constants.IS_TUTORIAL)
        fragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            FragmentStageModeScreen(1).apply { this.arguments = arguments },
            Constants.STAGE_MODE_SCREEN_FRAGMENT_TAG
        )
            .addToBackStack(null).commit()
    }

    private fun loadArcadeMode() {
        fragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            FragmentArcadeModeScreen(),
            Constants.ARCADE_MODE_SCREEN_FRAGMENT_TAG
        )
            .addToBackStack(null).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    @SuppressLint("InflateParams")
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
                fragmentManager.beginTransaction().replace(
                    R.id.fragmentContainer,
                    FragmentSettings(),
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

    private fun loadLevelScreen() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, FragmentLevelsScreen(), Constants.LEVELS_SCREEN_FRAGMENT_TAG)
            .addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragmentManager.findFragmentByTag(Constants.START_SCREEN_FRAGMENT_TAG)?.isVisible == true) {
            show3DotsToolBar()
        }
    }

    override fun show3DotsToolBar() {
        appToolbar.visibility = View.VISIBLE
    }

    override fun onSeekBarMainVolume(mainVolume: Int) {
        AudioManager.setGameVolume(mainVolume)
    }

    override fun onSeekBarSoundEffects(soundEffectsVolume: Int) {
        AudioManager.setEffectVolume(soundEffectsVolume)
    }

    override fun onResetGame() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(resources.getString(R.string.game_reset))
        alertDialogBuilder.setIcon(R.drawable.warning_icon)
        alertDialogBuilder.setMessage(R.string.Progress).setCancelable(false).setPositiveButton(R.string.Yes) { dialog, _ ->
            Constants.User.currentLevel = 1
            val newArrayList = ArrayList<StageInfo>()
            newArrayList.add(Constants.User.stageList[0])
            Constants.User.stageList = newArrayList
            DatabaseHelper.createOrUpdateUser(applicationContext, Constants.User)
            dialog.cancel()
        }
        alertDialogBuilder.setNegativeButton(R.string.No) { dialog, _ -> dialog.cancel() }

        val alert = alertDialogBuilder.create()
        alert.show()
    }

    override fun onExitFromSettingsFragment() {
        DatabaseHelper.createOrUpdateUser(applicationContext, Constants.User)
        onBackPressed()
    }

    private fun hide3DotsToolBar() {
        appToolbar.visibility = View.GONE
    }

    override fun onLevelsAdapterItemClicked(levelNumber: Int) {
        fragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            FragmentStageModeScreen(levelNumber),
            Constants.STAGE_MODE_SCREEN_FRAGMENT_TAG
        )
            .addToBackStack(null).commit()
    }

    override fun onFinishEditDialog(inputText: String) {
        Toast.makeText(this, "welcome $inputText", Toast.LENGTH_SHORT).show()
        createNewUser(inputText)
        mEditor.putBoolean(Constants.IS_FIRST_TIME_IN_APP, TRUE).commit()
        loadStartScreen()
        AudioManager.startGameMusic(this)
    }

    private fun createNewUser(nickname: String) {
        val newUserNote = RoomUserNote(
            UUID.randomUUID().toString(), nickname, 1, 20, 50, 3, ArrayList(),
            "", "", false, "0", ArrayList()
        )
        newUserNote.stageList.add(StageInfo(1, 1, 1, 1, 4, "1+1+1+1"))
        Constants.User = newUserNote
        DatabaseHelper.createOrUpdateUser(applicationContext, newUserNote)
    }

    private fun firstTimeInApp() {
        hide3DotsToolBar()
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, FragmentDialogNickName(), Constants.NICK_NAME_DIALOG_TAG)
            .commit()
    }

    override fun onPause() {
        super.onPause()
        AudioManager.stopGameMusic()
    }

    override fun onRestart() {
        super.onRestart()
        AudioManager.startGameMusic(this)
    }

    override fun backButtonPressedArcade() {
        fragmentManager.popBackStack()
    }

    override fun loadScoreBoardFromArcade() {
        fragmentManager.popBackStack()
        loadScoreBoard()

    }

    override fun restartArcadeGame() {
        fragmentManager.popBackStack()
        loadArcadeMode()
    }

    override fun arcadeModeHide3dotToolBar() {
        hide3DotsToolBar()
    }

    override fun backButtonPressedStage() {
        fragmentManager.popBackStack()
    }

    override fun onLevelsFragmentBackPressed() {
        fragmentManager.popBackStack()
    }

    override fun levelsFragmentClose3dotToolBar() {
        hide3DotsToolBar()
    }

    override fun onScoreBoardFragmentBackPressed() {
        fragmentManager.popBackStack()
    }

    override fun scoreBoardHide3dotToolBar() {
        hide3DotsToolBar()
    }
}

