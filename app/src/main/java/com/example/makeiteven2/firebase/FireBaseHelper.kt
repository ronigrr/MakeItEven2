package com.example.makeiteven2.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.makeiteven2.data_models.NameAndScoreInfo
import com.example.makeiteven2.extras.Constants
import com.google.firebase.database.*


object FireBaseHelper {

    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mFirebaseReference: DatabaseReference = mDatabase.getReference(Constants.SCOREBOARD_FIREBASE_REFERENCE)


    fun saveScoreToDatabaseScoreBoard(highScore: Int) {
        mFirebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    if (child.key.toString() == Constants.User.playerTokenId) {
                        val tempNameAndScoreInfo = child.getValue(NameAndScoreInfo::class.java) as NameAndScoreInfo
                        tempNameAndScoreInfo.playerScore = highScore
                        Log.v("database", child.toString())
                        mFirebaseReference.child(Constants.User.playerTokenId).setValue(tempNameAndScoreInfo)
                        return
                    }
                }
                mFirebaseReference.child(Constants.User.playerTokenId).setValue(NameAndScoreInfo(Constants.User.playerName, highScore))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "saveScoreToDatabaseScoreBoard was cancelled do to $error")
            }

        })
    }

    fun getScoreBoardListFromDataBase(data: MutableLiveData<ArrayList<NameAndScoreInfo>>) {
        val tempScoreBoardListForAdapter: ArrayList<NameAndScoreInfo> = ArrayList()
        val firebaseReferenceOrderByQuery = mFirebaseReference.orderByChild("playerScore")
        firebaseReferenceOrderByQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    Log.d("database", item.toString())
                    val tempNameAndScoreInfo = item.getValue(NameAndScoreInfo::class.java) as NameAndScoreInfo
                    tempScoreBoardListForAdapter.add(tempNameAndScoreInfo)
                }
                tempScoreBoardListForAdapter.reverse()
                data.value = tempScoreBoardListForAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "Failed to read value.", error.toException())
            }
        })
    }

}