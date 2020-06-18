package com.example.makeiteven2.room

import androidx.room.*
import com.example.makeiteven2.room.RoomUserNote

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    suspend fun getNotes():List<RoomUserNote>

    @Query("SELECT * FROM note_table WHERE player_token_id = :playerTokenId")
    suspend fun getNoteById(playerTokenId : String): RoomUserNote

    @Delete
    suspend fun deleteNote(userNote : RoomUserNote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNote(userNote : RoomUserNote):Long

    @Update
    suspend fun updateNote(userNote: RoomUserNote)

}