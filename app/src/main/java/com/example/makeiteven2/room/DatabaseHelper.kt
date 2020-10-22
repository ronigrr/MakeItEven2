package com.example.makeiteven2.room

import android.content.Context
import com.example.makeiteven2.data_models.NameAndScoreInfo
import com.example.makeiteven2.data_models.StageInfo
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

    fun saveCoinsToDataBase(context: Context, numberCoinsLeft: Int) {
        getDataBase(context)
        Constants.User.coinsLeft = numberCoinsLeft
        GlobalScope.launch { mNoteDao.insertOrUpdateNote(Constants.User) }
    }


    suspend fun loadUserToConstants(context: Context) {
        getDataBase(context)
        Constants.User = mNoteDao.getNotes()[0]

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

    fun addCoins(context: Context, coinsToAdd: Int) {
        getDataBase(context)
        Constants.User.coinsLeft += coinsToAdd
        GlobalScope.launch {
            mNoteDao.insertOrUpdateNote(Constants.User)
        }
    }

    fun setGiftGiven(context: Context, isGiftGiven: Boolean) {
        getDataBase(context)
        Constants.User.isCoinsGiftGiven = isGiftGiven
        GlobalScope.launch {
            mNoteDao.insertOrUpdateNote(Constants.User)
        }
    }

    fun setGiftStartTimeStamp(context: Context, timestamp: String) {
        getDataBase(context)
        Constants.User.coinsGivenTimeStampStart = timestamp
        GlobalScope.launch {
            mNoteDao.insertOrUpdateNote(Constants.User)
        }
    }

    fun setPlayerArcadeMaxScore(context: Context, highScore: String) {
        getDataBase(context)
        Constants.User.arcadeHighScore = highScore
        GlobalScope.launch {
            mNoteDao.insertOrUpdateNote(Constants.User)
        }
    }

    fun saveCurrentScoreBoard(context: Context, scoreBoardArray: List<NameAndScoreInfo>?) {
        getDataBase(context)
        Constants.User.arcadeScoreBoard = scoreBoardArray as ArrayList<NameAndScoreInfo>
        GlobalScope.launch {
            mNoteDao.insertOrUpdateNote(Constants.User)
        }
    }

    fun getCurrentScoreBoard(): ArrayList<NameAndScoreInfo> {
        return Constants.User.arcadeScoreBoard
    }
    fun changePlayerNickname(context: Context, nickName: String) {
        getDataBase(context)
        Constants.User.playerName = nickName
        GlobalScope.launch {
            mNoteDao.insertOrUpdateNote(Constants.User)
        }
    }

    fun getStageInfoList(): ArrayList<StageInfo> {
        return Constants.User.stageList
    }
}

