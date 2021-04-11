package com.yoyoG.makeiteven2.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.yoyoG.makeiteven2.data_models.NameAndScoreInfo
import com.yoyoG.makeiteven2.extras.Constants


object FireBaseHelper {

    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mFirebaseScoreBoardReference: DatabaseReference = mDatabase.getReference(Constants.SCOREBOARD_FIREBASE_REFERENCE)
    private val mFireBaseUserReference: DatabaseReference = mDatabase.getReference(Constants.USER_FIREBASE_REFERENCE)


    fun saveScoreToDatabaseScoreBoard(highScore: Int) {
        mFirebaseScoreBoardReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    if (child.key.toString() == Constants.User.playerTokenId) {
                        val tempNameAndScoreInfo = child.getValue(NameAndScoreInfo::class.java) as NameAndScoreInfo
                        tempNameAndScoreInfo.playerScore = highScore
                        Log.v("database", child.toString())
                        mFirebaseScoreBoardReference.child(Constants.User.playerTokenId).setValue(tempNameAndScoreInfo)
                        return
                    }
                }
                mFirebaseScoreBoardReference.child(Constants.User.playerTokenId).setValue(NameAndScoreInfo(Constants.User.playerName, highScore))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "saveScoreToDatabaseScoreBoard was cancelled do to $error")
            }

        })
    }

    fun getScoreBoardListFromDataBase(data: MutableLiveData<ArrayList<NameAndScoreInfo>>) {
        val tempScoreBoardListForAdapter: ArrayList<NameAndScoreInfo> = ArrayList()
        val firebaseReferenceOrderByQuery = mFirebaseScoreBoardReference.orderByChild("playerScore")
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

    fun saveUserToDatabase(){
        mFireBaseUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    if (child.key.toString() == Constants.User.playerTokenId) {
                        Log.v("database", child.toString())
                        mFireBaseUserReference.child(Constants.User.playerTokenId).setValue(Constants.User)
                        return
                    }
                }
                mFireBaseUserReference.child(Constants.User.playerTokenId).setValue(Constants.User)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "saveScoreToDatabaseScoreBoard was cancelled do to $error")
            }

        })
    }

    fun updateScoreBoardUserNickName(nickName: String) {
        mFirebaseScoreBoardReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    if (child.key.toString() == Constants.User.playerTokenId) {
                        val tempNameAndScoreInfo = child.getValue(NameAndScoreInfo::class.java) as NameAndScoreInfo
                        tempNameAndScoreInfo.playerName = nickName
                        Log.v("database", child.toString())
                        mFirebaseScoreBoardReference.child(Constants.User.playerTokenId).setValue(tempNameAndScoreInfo)
                        return
                    }
                }
                mFirebaseScoreBoardReference.child(Constants.User.playerTokenId).setValue(NameAndScoreInfo(nickName, Constants.User.arcadeHighScore.toInt()))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "saveScoreToDatabaseScoreBoard was cancelled do to $error")
            }

        })
    }

}