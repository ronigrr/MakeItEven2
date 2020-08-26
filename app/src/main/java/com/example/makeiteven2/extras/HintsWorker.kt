package com.example.makeiteven2.extras

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.makeiteven2.room.DatabaseHelper
import com.example.makeiteven2.room.RoomNoteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class HintsWorker(private val appContext: Context,workerParams:WorkerParameters): Worker(appContext,workerParams){
    override fun doWork(): Result {
        val mNoteDatabase = RoomNoteDatabase.getInstance(appContext)
        val mNoteDao = mNoteDatabase.roomNoteDao()
        GlobalScope.launch {
            Constants.User = mNoteDao.getNotes()[0]
            Constants.User.isHintGiftGiven = false
            Constants.User.hintTimeStampStart=""
            mNoteDao.insertOrUpdateNote(Constants.User)
            Log.v("test","GiftsGiven = True from hints worker")
        }
        return Result.success()
    }
    //TODO:maybe dont need it,we only need a countdown yo enable free hint btn
}