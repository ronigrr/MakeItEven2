package com.example.makeiteven2.firebase

import android.content.Context
import android.util.Log
import com.example.makeiteven2.adapters.ScoreBoardCellAdapter
import com.example.makeiteven2.data_models.NameAndScoreInfo
import com.example.makeiteven2.extras.Constants
import com.google.firebase.database.*


object FireBaseHelper {

    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mFirebaseReference: DatabaseReference

    private fun getFireBaseDataBase(context: Context){
        //MUST call this fun on each public fun!!!
        mDatabase = FirebaseDatabase.getInstance()
    }

    fun saveScoreToDatabaseScoreBoard(context: Context,highScore : Int){
        getFireBaseDataBase(context)
        mFirebaseReference = mDatabase.getReference(Constants.SCOREBOARD_FIREBASE_REFERENCE)
        mFirebaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children)
                {
                    if (child.key.toString()==Constants.User.playerTokenId)
                    {
                    val tempNameAndScoreInfo = child.getValue(NameAndScoreInfo::class.java) as NameAndScoreInfo
                        tempNameAndScoreInfo.playerScore=highScore
                        Log.v("database",child.toString())
                        mFirebaseReference.child(Constants.User.playerTokenId).setValue(tempNameAndScoreInfo)
                        return
                    }
                }
                mFirebaseReference.child(Constants.User.playerTokenId).setValue(NameAndScoreInfo(Constants.User.playerName,highScore))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("database","saveScoreToDatabaseScoreBoard was cancelled do to $error")
            }

        })
    }

    fun getScoreBoardListFromDataBase(context: Context,adapter : ScoreBoardCellAdapter){
        getFireBaseDataBase(context)
        val tempScoreBoardListForAdapter : ArrayList<NameAndScoreInfo> = ArrayList()
        val mFirebaseReferenceOrderByQuery = mDatabase.getReference(Constants.SCOREBOARD_FIREBASE_REFERENCE)
            .orderByChild("playerScore")
        mFirebaseReferenceOrderByQuery.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children)
                {
                    Log.d("database",item.toString())
                    val tempNameAndScoreInfo = item.getValue(NameAndScoreInfo::class.java) as NameAndScoreInfo
                    tempScoreBoardListForAdapter.add(tempNameAndScoreInfo)
                }
                tempScoreBoardListForAdapter.reverse()
                adapter.updateScoreBoardList(tempScoreBoardListForAdapter)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "Failed to read value.", error.toException())
            }
        })
    }

}