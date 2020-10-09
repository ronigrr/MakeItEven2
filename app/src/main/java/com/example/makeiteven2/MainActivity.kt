package com.example.makeiteven2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.example.makeiteven2.adapters.LevelsAdapter
import com.example.makeiteven2.data_models.NameAndScoreInfo
import com.example.makeiteven2.data_models.StageInfo
import com.example.makeiteven2.extras.AudioManager
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.fragments.*
import com.example.makeiteven2.intefaces.*
import com.example.makeiteven2.room.DatabaseHelper
import com.example.makeiteven2.room.FireBaseHelper
import com.example.makeiteven2.room.RoomUserNote
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_start_screen.*
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), IFragmentsStartsScreenListener
    , IFragmentSettingsListener, LevelsAdapter.ILevelsAdapter,
    FragmentDialogNickName.DialogListener, IFragmentStageModeListener, IFragmentArcadeModeListener, IFragmentLevelsScreenListener,IFragmentScoreBoardScreenListener{

    private val fragmentManager = supportFragmentManager

    private lateinit var mSharedPref: SharedPreferences
    private lateinit var mEditor: Editor

    private lateinit var appToolbar: Toolbar

    private lateinit var appUpdateManager : AppUpdateManager
    private lateinit var appUpdateInfoTask : Task<AppUpdateInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init3DotToolBar()
        initUpdateManager()

        mSharedPref = applicationContext.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
        mEditor = mSharedPref.edit()
        AudioManager.getInstance(this)

//        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                // For a flexible update, use AppUpdateType.FLEXIBLE
//                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
//                // Request the update.
//            }
//            else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE) {
//                //continue
//                 }
//        }
        startLoadingApp()
    }

    private fun startLoadingApp() {
        if (mSharedPref.getBoolean(Constants.IS_FIRST_TIME_IN_APP, FALSE) == FALSE) {
            Handler().postDelayed(Runnable {
                setMainActivityVisible()
                firstTimeInApp()
            }, 5000)
        } else {
            loadUser()
            Handler().postDelayed(Runnable {
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
        DatabaseHelper.loadUserToConstants(applicationContext)
    }

    private fun loadStartScreen() {
        setMainActivityVisible()
        fragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, FragmentStartScreen(), Constants.START_SCREEN_FRAGMENT_TAG)
            .commit()
    }

    override fun onStartScreenFragmentButtonClicked(view: View) {
        when (view.id) {
            btnStageMode.id -> loadStageModeLevelScreen()
            btnArcadeMode.id -> loadArcadeMode()
            btnScoreBoard.id -> loadScoreBoard()
            btnTutorial.id -> {
                loadStageModeWithTutorial()
            }
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
        hide3DotsToolBar()
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

    private fun loadStageModeLevelScreen() {

        fragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            FragmentLevelsScreen(),
            Constants.LEVELS_SCREEN_FRAGMENT_TAG
        )
            .addToBackStack(null).commit()
        hide3DotsToolBar()
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
        //TODO: may not work properly,need to check it after the game is ready (check the code inside positive btn)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(resources.getString(R.string.game_reset))
        alertDialogBuilder.setIcon(R.drawable.warning_icon)
        alertDialogBuilder.setMessage(R.string.Progress).setCancelable(false).setPositiveButton(R.string.Yes) { dialog, _ ->
            Constants.User.currentLevel = 1
            DatabaseHelper.createOrUpdateUser(applicationContext, Constants.User)
            //DataStore.getInstance(this@StartScreenActivity).resetLevels()
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
        AudioManager.startGameMusic()
    }

    private fun createNewUser(nickname: String) {
        val newUserNote = RoomUserNote(
            UUID.randomUUID().toString(), nickname, 1, 20, 50, 3, ArrayList(),
            "", "", false,"0"
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
        AudioManager.startGameMusic()
    }

    override fun backButtonPressedArcade() {
        fragmentManager.popBackStack()
    }

    override fun loadScoreBoardFromArcade() {
        fragmentManager.popBackStack()
        loadScoreBoard()

    }

    override fun backButtonPressedStage() {
        fragmentManager.popBackStack()
    }

    override fun onLevelsFragmentBackPressed() {
        fragmentManager.popBackStack()
    }

    override fun onScoreBoardFragmentBackPressed() {
        fragmentManager.popBackStack()
    }

    override fun hide3dotToolBar() {
        hide3DotsToolBar()
    }
}

//TODO: need to licence , super_duper,tada,wa wa
