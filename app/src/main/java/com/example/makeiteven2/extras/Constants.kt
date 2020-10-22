package com.example.makeiteven2.extras

import android.content.Context
import android.util.DisplayMetrics
import com.example.makeiteven2.room.RoomUserNote
import com.google.android.gms.ads.rewarded.RewardedAd


object Constants {

    const val IS_FIRST_TIME_IN_STAGE_MODE: String = "first_time_in_stagemode"
    const val SCOREBOARD_SCREEN_FRAGMENT_TAG: String = "scoreboard_screen_fragment"
    const val IS_TUTORIAL = "is_tutorial"
    const val TEST_COUNTDOWN_10_SECONDS_IN_MILLIS: Long = (0.2 * 1000 * 60).toLong()
    const val START_COUNTDOWN_ARCADE_TIMER_IN_MILLIS: Long = (1* 1000 * 60).toLong()
    const val ARCADE_TIMER = "arcade_timer"
    const val COINS_TIMER = "coins_timer"
    const val TUTORIAL_GAME_TYPE = "tutorial_game_type"
    const val STAGE_GAME_TYPE = "stage_game_type"
    const val ARCADE_GAME_TYPE = "arcade_game_type"
    const val START_SCREEN_FRAGMENT_TAG = "start_screen_fragment"
    const val SETTINGS_SCREEN_FRAGMENT_TAG = "settings_screen_fragment"
    const val LEVELS_SCREEN_FRAGMENT_TAG = "levels_screen_fragment"
    const val STAGE_MODE_SCREEN_FRAGMENT_TAG = "stage_mode_screen_fragment"
    const val ARCADE_MODE_SCREEN_FRAGMENT_TAG = "arcade_mode_screen_fragment"
    const val ROOM_DATA_BASE_NAME = "note_table"
    const val NICK_NAME_kYE = "nickname"
    const val NICK_NAME_DIALOG_TAG = "nickname_dialog"
    const val SHARED_PREFS = "sharedPref"
    const val IS_FIRST_TIME_IN_APP = "is_first_time_in_app"
    const val WIN_DIALOG = "win_dialog"
    const val LOSE_DIALOG = "lose_dialog"
    const val ARCADE_END_DIALOG = "arcade_end_dialog"
    const val SHOWCASE_ID = "showcase_id"
    const val TIME_UNITS_FOR_HINTS_IN_MILLI: Long = 21600000
    const val GIFT_HINTS_TO_GIVE = 1
    const val ADD_MOB = "ca-app-pub-4722610653423402/4300443792"
    const val ADD_MOB_TEST = "ca-app-pub-3940256099942544/5224354917"
    const val SCOREBOARD_FIREBASE_REFERENCE = "scoreBoard"

    fun calculateNoOfColumns(context: Context, columnWidthDp: Int): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    lateinit var User: RoomUserNote
    lateinit var  rewardedAd : RewardedAd

}