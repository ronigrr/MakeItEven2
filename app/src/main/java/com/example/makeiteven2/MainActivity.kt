package com.example.makeiteven2

//import com.example.makeiteven2.extras.Constants.mAudioManager
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
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
import com.example.makeiteven2.fragments.*
import com.example.makeiteven2.intefaces.*
import com.example.makeiteven2.room.DatabaseHelper
import com.example.makeiteven2.room.RoomUserNote
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_start_screen.*
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), IFragmentsStartsScreenListener
    , IFragmentSettingsListener, LevelsAdapter.ILevelsAdapter,
    FragmentDialogNickName.DialogListener, IFragmentStageModeListener, IFragmentArcadeModeListener, IFragmentLevelsScreenListener {

    private val fragmentManager = supportFragmentManager

    private lateinit var uiHandler: Handler

    private lateinit var mSharedPref: SharedPreferences
    private lateinit var mEditor: Editor

    private lateinit var appToolbar: Toolbar

    //private lateinit var mAudioManager : AudioManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init3DotToolBar()

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

    private fun init3DotToolBar() {
        appToolbar = toolBar as Toolbar
        appToolbar.title = " "
        setSupportActionBar(appToolbar)
    }

    private fun loadUser() {
        DatabaseHelper.loadUserToConstants(applicationContext)
    }

    private fun loadStartScreen() {
        fragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, FragmentStartScreen(), Constants.START_SCREEN_FRAGMENT_TAG)
            .commit()
    }

    override fun onStartScreenFragmentButtonClicked(view: View) {
        when (view.id) {
            btnStageMode.id -> loadStageModeLevelScreen()
            btnArcadeMode.id -> loadArcadeMode()
            btnScoreBoard.id -> { }
            btnTutorial.id -> {loadStageModeWithTutorial()}
        }
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
        //TODO: Bug that reloads the recyclerview in levelsScreenFragment when you backpress and press stagemode again
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
        mEditor.putBoolean(Constants.SHARED_KEY_IS_USER_EXISTS, TRUE).commit()
        loadStartScreen()
    }

    private fun createNewUser(nickname: String) {
        val newUserNote = RoomUserNote(
            UUID.randomUUID().toString(), nickname, 1, 20, 50, 3, ArrayList(),
            "", "", false
        )
        newUserNote.stageList.add(StageInfo(1,1,1,1,4,"1+1+1+1"))
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

    override fun backButtonPressedStage() {
        fragmentManager.popBackStack()
    }

    override fun onLevelsFragmentBackPressed() {
        fragmentManager.popBackStack()
    }


}

//TODO: need to licence , super_duper,tada,wa wa
