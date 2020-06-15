package com.example.makeiteven2.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.makeiteven2.extras.Constants

@Entity(
    tableName = Constants.ROOM_DATA_BASE_NAME
)

data class RoomUserNote (

    @PrimaryKey
    @ColumnInfo(name = "player_token_id")
    val playerTokenId : String,

    @ColumnInfo(name = "player_name")
    val playerName : String?,

    @ColumnInfo(name= "current_level")
    val currentLevel : Int?,

    @ColumnInfo(name = "main_sound_level")
    val mainSoundLevel: Int?,

    @ColumnInfo(name = "sound_effects_level")
    val soundEffectsLevel: Int?,

    @ColumnInfo(name = "hints_left")
    val hintsLeft: Int?
)