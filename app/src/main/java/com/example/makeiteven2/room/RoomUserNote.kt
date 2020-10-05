package com.example.makeiteven2.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.makeiteven2.data_models.StageInfo
import com.example.makeiteven2.extras.Constants

@Entity(
    tableName = Constants.ROOM_DATA_BASE_NAME
)

data class RoomUserNote(

    @PrimaryKey
    @ColumnInfo(name = "player_token_id")
    val playerTokenId: String,

    @ColumnInfo(name = "player_name")
    var playerName: String,

    @ColumnInfo(name = "current_level")
    var currentLevel: Int,

    @ColumnInfo(name = "main_sound_level")
    var mainSoundLevel: Int,

    @ColumnInfo(name = "sound_effects_level")
    var soundEffectsLevel: Int,

    @ColumnInfo(name = "coins_left")
    var coinsLeft: Int,

    @ColumnInfo(name = "stages")
    var stageList: ArrayList<StageInfo>,

    @ColumnInfo(name = "coins_time_stamp")
    var coinsGivenTimeStampStart: String,

    @ColumnInfo(name = "time_work_done")
    var timeWorkDone: String,

    @ColumnInfo(name = "isHint_given")
    var isCoinsGiftGiven: Boolean,

    @ColumnInfo(name = "arcade_highScore")
    var arcadeHighScore : String="0"
)