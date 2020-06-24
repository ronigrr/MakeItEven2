package com.example.makeiteven2.extras

import com.example.makeiteven2.room.RoomUserNote

object Constants {

    const val START_SCREEN_FRAGMENT_TAG = "start_screen_fragment"
    const val SETTINGS_SCREEN_FRAGMENT_TAG="settings_screen_fragment"
    const val LEVELS_SCREEN_FRAGMENT_TAG= "levels_screen_fragment"
    const val STAGE_MODE_SCREEN_FRAGMENT_TAG="stage_mode_screen_fragment"
    const val ARCADE_MODE_SCREEN_FRAGMENT_TAG="arcade_mode_screen_fragment"
    const val ROOM_DATA_BASE_NAME="note_table"
    const val NICK_NAME_kYE="nickname"
    const val NICK_NAME_DIALOG_TAG = "nickname_dialog"
    const val SHARED_PREFS = "sharedPref"
    const val SHARED_KEY_IS_USER_EXISTS = "is_user_exists"

    lateinit var User : RoomUserNote

    //Todo: note sure if i want to do it like this "lateinit var mAudioManager: AudioManager"


}