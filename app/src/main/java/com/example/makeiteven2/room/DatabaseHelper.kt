package com.example.makeiteven2.room

import android.content.Context
import com.example.makeiteven2.data_models.StageInfo
import com.example.makeiteven2.extras.AudioManager
import com.example.makeiteven2.extras.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


object DatabaseHelper {
    private lateinit var mNoteDatabase: RoomNoteDatabase
    private lateinit var mNoteDao: NoteDao

    private fun getDataBase(context: Context) {
        //MUST call this fun on each public fun!!!
        mNoteDatabase = RoomNoteDatabase.getInstance(context)
        mNoteDao = mNoteDatabase.roomNoteDao()
    }

    fun saveHintsToDataBase(context: Context, numberHintsLeft: Int) {
        getDataBase(context)
        Constants.User.hintsLeft = numberHintsLeft
        GlobalScope.launch { mNoteDao.insertOrUpdateNote(Constants.User) }
    }


    fun loadUserToConstants(context: Context) {
        getDataBase(context)
        GlobalScope.launch {
            Constants.User = mNoteDao.getNotes()[0]
            AudioManager.startGameMusic()
        }
    }

    fun createOrUpdateUser(context: Context, newUserNote: RoomUserNote) {
        getDataBase(context)
        GlobalScope.launch { mNoteDao.insertOrUpdateNote(newUserNote) }
    }

    fun saveStageInfo(context: Context, stageInfo: StageInfo) {
        getDataBase(context)
        Constants.User.stageList.add(stageInfo)
        GlobalScope.launch { mNoteDao.insertOrUpdateNote(Constants.User) }
    }

    fun saveCurrentStage(context: Context, currentStage: Int) {
        getDataBase(context)
        Constants.User.currentLevel = currentStage
        GlobalScope.launch { mNoteDao.insertOrUpdateNote(Constants.User) }
    }

    fun addHints(context: Context, hintsToAdd: Int) {
        getDataBase(context)
        Constants.User.hintsLeft += hintsToAdd
        GlobalScope.launch {
            mNoteDao.insertOrUpdateNote(Constants.User)
        }
    }

    fun setGiftGiven(context: Context, isGiftGiven: Boolean) {
        getDataBase(context)
        Constants.User.isHintGiftGiven = isGiftGiven
        GlobalScope.launch {
            mNoteDao.insertOrUpdateNote(Constants.User)
        }
    }

    fun setGiftStartTimeStamp(context: Context, timestamp: String) {
        getDataBase(context)
        Constants.User.hintTimeStampStart = timestamp
        GlobalScope.launch {
            mNoteDao.insertOrUpdateNote(Constants.User)
        }
    }
}