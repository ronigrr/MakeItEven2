package com.example.makeiteven2

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.makeiteven2.adapters.LevelsAdapter
import com.example.makeiteven2.extras.AudioManager
import com.example.makeiteven2.extras.Constants
//import com.example.makeiteven2.extras.Constants.mAudioManager
import com.example.makeiteven2.fragments.*
import com.example.makeiteven2.room.DatabaseHelper
import com.example.makeiteven2.room.RoomUserNote
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_start_screen.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), FragmentStartScreen.IFragmentsStartsScreenCallback
    , FragmentSettings.SettingsFragmentCallBack, LevelsAdapter.ILevelsAdapter,
    FragmentDialogNickName.DialogListener, IFragmentStageModeListener {

    private val fragmentManager = supportFragmentManager
    private val fragmentStartScreen: FragmentStartScreen = FragmentStartScreen()
    private val fragmentSettings: FragmentSettings = FragmentSettings()
    private val fragmentLevelsScreen: FragmentLevelsScreen = FragmentLevelsScreen()
    private val fragmentArcadeModeScreen: FragmentArcadeModeScreen = FragmentArcadeModeScreen()
    private val dialogFragmentFragmentNickName: FragmentDialogNickName = FragmentDialogNickName()

    //    private lateinit var mNoteDatabase : RoomNoteDatabase
//    private lateinit var mNoteDao: NoteDao
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var uiHandler: Handler

    private lateinit var mSharedPref: SharedPreferences
    private lateinit var mEditor: Editor

    private lateinit var appToolbar: Toolbar

    //private lateinit var mAudioManager : AudioManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolBar()
//        mNoteDatabase = RoomNoteDatabase.getInstance(applicationContext)
//        mNoteDao = mNoteDatabase.roomNoteDao()
        uiHandler = Handler()

        mSharedPref = applicationContext.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
        mEditor = mSharedPref.edit()
        AudioManager.getInstance(this)

        if (mSharedPref.getBoolean(Constants.SHARED_KEY_IS_USER_EXISTS, FALSE) == FALSE) {
            firstTimeInApp()
        } else {
            loadUser()
            loadStartScreen()
        }
    }

    private fun initToolBar() {
        appToolbar = toolBar as Toolbar
        appToolbar.title = " "
        setSupportActionBar(appToolbar)
    }

    private fun loadUser() {
        DatabaseHelper.loadUserToConstants(applicationContext)
    }

    private fun loadStartScreen() {
        fragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, fragmentStartScreen, Constants.START_SCREEN_FRAGMENT_TAG)
            .commit()
    }

    override fun onStartScreenFragmentButtonClicked(view: View) {
        when (view.id) {
            btnStageMode.id -> loadStageModeLevelScreen()
            btnArcadeMode.id -> loadArcadeMode()
            btnScoreBoard.id -> {
            }
            btnTutorial.id -> Toast.makeText(this, "Tutorial", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadArcadeMode() {
        fragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            fragmentArcadeModeScreen,
            Constants.ARCADE_MODE_SCREEN_FRAGMENT_TAG
        )
            .addToBackStack(null).commit()
        hideToolBar()
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

    private fun loadStageModeLevelScreen() {

        fragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            fragmentLevelsScreen,
            Constants.LEVELS_SCREEN_FRAGMENT_TAG
        )
            .addToBackStack(null).commit()
        hideToolBar()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragmentManager.findFragmentById(R.id.fragmentContainer)
                ?.equals(fragmentStartScreen)!!
        ) {
            showToolBar()
        }
        //TODO: Bug that reloads the recyclerview in levelsScreenFragment when you backpress and press stagemode again
    }

    override fun showToolBar() {
        appToolbar.visibility = View.VISIBLE
    }

    override fun onSeekBarMainVolume(mainVolume: Int) {
        AudioManager.setGameVolume(mainVolume)
    }

    override fun onSeekBarSoundEffects(soundEffectsVolume: Int) {
        AudioManager.setEffectVolume(soundEffectsVolume)
    }

    override fun onResetGame() {
        //TODO: may not work properly,need to check it after the game i ready (check the code inside positive btn)
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

    private fun hideToolBar() {
        appToolbar.visibility = View.GONE
    }

    override fun levelsAdapterItemClicked(levelNumber: Int) {
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
        mEditor.putBoolean(Constants.SHARED_KEY_IS_USER_EXISTS, TRUE).commit()
        loadStartScreen()
    }

    private fun createNewUser(nickname: String) {
        val newUserNote = RoomUserNote(
            UUID.randomUUID().toString(), nickname, 1, 50, 50, 3, ArrayList()
        )
        Constants.User = newUserNote
        DatabaseHelper.createOrUpdateUser(applicationContext, newUserNote)
    }

    private fun firstTimeInApp() {
        hideToolBar()
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, dialogFragmentFragmentNickName, Constants.NICK_NAME_DIALOG_TAG)
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

    override fun backButtonPressed() {
        TODO("Not yet implemented")
    }
}

//TODO: need to licence arcade_win , super_duper,tada,wa wa and also for the owl image
