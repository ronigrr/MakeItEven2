package com.yoyoG.makeiteven2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.tasks.Task
import com.yoyoG.makeiteven2.adapters.LevelsAdapter
import com.yoyoG.makeiteven2.data_models.StageInfo
import com.yoyoG.makeiteven2.dialogs.DialogStore
import com.yoyoG.makeiteven2.extras.Constants
import com.yoyoG.makeiteven2.fragments.*
import com.yoyoG.makeiteven2.intefaces.*
import com.yoyoG.makeiteven2.managers.AudioManager
import com.yoyoG.makeiteven2.managers.GoogleAddManager
import com.yoyoG.makeiteven2.managers.RetentionManager
import com.yoyoG.makeiteven2.managers.ShearedPrefManager
import com.yoyoG.makeiteven2.room.DatabaseHelper
import com.yoyoG.makeiteven2.room.RoomUserNote
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_start_screen.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), IFragmentsStartsScreenListener, IFragmentSettingsListener, LevelsAdapter.ILevelsAdapter,
    FragmentDialogNickName.DialogListener, IFragmentStageModeListener, IFragmentArcadeModeListener, IFragmentLevelsScreenListener,
    IFragmentScoreBoardScreenListener {

    private val fragmentManager = supportFragmentManager
    private lateinit var appToolbar: Toolbar

    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var appUpdateInfoTask: Task<AppUpdateInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("lifecycle", "on create")
        hideNavBars()
        setContentView(R.layout.activity_main)
        init3DotToolBar()
        //initUpdateManager()
        startLoadingApp()
        activeAd()
    }

    private fun hideNavBars() {
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                SYSTEM_UI_FLAG_FULLSCREEN or SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private fun rateMe() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }


    private fun activeAd() {
        Handler(Looper.getMainLooper()).postDelayed({
            GoogleAddManager.loadRewardAD(applicationContext)
            GoogleAddManager.loadInterstitialAd(applicationContext)
        }, 7 * 1000)
    }

    private fun startLoadingApp() {
        if (ShearedPrefManager.getIsFirstTimeInApp(this) == FALSE) {
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
            Handler(Looper.getMainLooper()).postDelayed({
                //AudioManager.getInstance(this@MainActivity).playGameBeginAndStartLoop()
                Constants.liveDataCoins.value = Constants.User.coinsLeft
            }, 4000)
        }
    }

    private fun loadStartScreen() {
        try {
            setMainActivityVisible()
            fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, FragmentStartScreen(), Constants.START_SCREEN_FRAGMENT_TAG)
                .commit()
        } catch (e: Exception) {
            finish()
            exitProcess(0)
        }
    }

    override fun onStartScreenFragmentButtonClicked(view: View) {
        when (view.id) {
            btnStageMode.id -> loadLevelScreen()
            btnArcadeMode.id -> loadArcadeMode()
            btnScoreBoard.id -> loadScoreBoard()
            btnTutorial.id -> loadStageModeWithTutorial()
            btnRateUs.id -> rateMe()
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

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        super.onMenuOpened(featureId, menu)
        Log.e("onMenuOpened", "onMenuOpened")
        Handler(Looper.getMainLooper()).postDelayed({ hideNavBars() }, 100)
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
                appToolbar.visibility = GONE
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
        appToolbar.visibility = VISIBLE
    }

    override fun onSeekBarMainVolume(mainVolume: Int) {
        AudioManager.getInstance(this).setGameVolume(mainVolume)
    }

    override fun onSeekBarSoundEffects(soundEffectsVolume: Int) {
        AudioManager.getInstance(this).setEffectVolume(soundEffectsVolume)
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
        appToolbar.visibility = GONE
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
        ShearedPrefManager.setIsFirstTimeInApp(this, TRUE)
        loadStartScreen()
        AudioManager.getInstance(this).playGameBeginAndStartLoop()
    }

    private fun createNewUser(nickname: String) {
        val newUserNote = RoomUserNote(
            UUID.randomUUID().toString(), nickname, 1, 10, 50, 3, ArrayList(),
            "", "", false, "0", ArrayList()
        )
        newUserNote.stageList.add(StageInfo(1, 1, 1, 1, 4, "1+1+1+1"))
        Constants.User = newUserNote
        //update coins to live data
        Constants.liveDataCoins.value = newUserNote.coinsLeft
        DatabaseHelper.createOrUpdateUser(applicationContext, newUserNote)
        Constants.liveDataCoins.value = Constants.User.coinsLeft
    }

    private fun firstTimeInApp() {
        hide3DotsToolBar()
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, FragmentDialogNickName(), Constants.NICK_NAME_DIALOG_TAG)
            .commit()
    }

    override fun onStop() {
        super.onStop()
        AudioManager.getInstance(this).pauseCurrentLoopMusic()
        RetentionManager.getInstance(applicationContext).setNotification(Constants.NOTIFICATION_COUNTDOWN_3_DAYS_IN_MILLIS)
        Log.e("lifecycel", "on stop mainactivity")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("lifecycel", "on restart mainactivity")
    }

    override fun onPause() {
        super.onPause()
        AudioManager.getInstance(this).pauseCurrentLoopMusic()
        Log.e("lifecycel", "on pause mainactivity")
    }

    //    override fun onResume() {
//        super.onResume()
//        supportFragmentManager.fragments.lastOrNull()?.let { currentFragment ->
//            AudioManager.getInstance(this).playLoopMusicForSpecificFragment(currentFragment.tag!!)
//        }
//    }
    override fun onDestroy() {
        super.onDestroy()
        AudioManager.getInstance(this).stopCurrentLoopMusic()
        Log.e("lifecycel", "on destroy mainactivity")
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

    override fun onStart() {
        super.onStart()
        RetentionManager.getInstance(applicationContext).cancelNotification()
        Log.e("lifecycel", "on start mainactivity")

    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager.fragments.lastOrNull()?.let { currentFragment ->
            AudioManager.getInstance(this).playLoopMusicForSpecificFragment(currentFragment.tag!!)
        }
        hideNavBars()
        Log.e("lifecycel", "on resume mainactivity")
    }
}

