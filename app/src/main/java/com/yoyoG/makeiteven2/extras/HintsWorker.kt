package com.yoyoG.makeiteven2.extras

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yoyoG.makeiteven2.room.RoomNoteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HintsWorker(private val appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val mNoteDatabase = RoomNoteDatabase.getInstance(appContext)
        val mNoteDao = mNoteDatabase.roomNoteDao()
        GlobalScope.launch {
            Constants.User = mNoteDao.getNotes()[0]
            Constants.User.isCoinsGiftGiven = false
            Constants.User.coinsGivenTimeStampStart = ""
            mNoteDao.insertOrUpdateNote(Constants.User)
            Log.v("gifts", "GiftsGiven = True from hints worker")
        }
        return Result.success()
    }

}