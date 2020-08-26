package com.example.makeiteven2.extras

import com.example.makeiteven2.room.RoomUserNote

object Constants {


    const val START_COUNTDOWN_ARCADE_TIMER_IN_MILLIS: Long= (1.5*1000*60).toLong()
    const val ARCADE_TIMER = "arcade_timer"
    const val HINTS_TIMER = "hint_timer"
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
    const val SHARED_KEY_IS_USER_EXISTS = "is_user_exists"
    const val WIN_DIALOG = "win_dialog"
    const val LOSE_DIALOG = "lose_dialog"
    const val SHOWCASE_ID = "showcase_id"
    const val ADD_HINTS ="add_hints"
    const val TIME_UNITS_FOR_HINTS_IN_MILLI : Long = 21600000
    const val GIFT_HINTS_TO_GIVE = 1

    lateinit var User: RoomUserNote

}