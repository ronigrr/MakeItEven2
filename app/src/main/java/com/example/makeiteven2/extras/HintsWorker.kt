package com.example.makeiteven2.extras

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.makeiteven2.room.DatabaseHelper
import com.example.makeiteven2.room.RoomNoteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.logging.Handler

class HintsWorker(appContext: Context,workerParams:WorkerParameters): Worker(appContext,workerParams){
    val context = appContext
    override fun doWork(): Result {
        val mNoteDatabase = RoomNoteDatabase.getInstance(context)
        val  mNoteDao = mNoteDatabase.roomNoteDao()
        GlobalScope.launch{
            val mUser = mNoteDao.getNotes()[0]
            Constants.User.hintsLeft++
            mNoteDao.insertOrUpdateNote(mUser)
            Log.v("hint","hint++")
        }

        return Result.success()
    }
    //TODO:maybe dont need it,we only need a countdown yo enable free hint btn
}