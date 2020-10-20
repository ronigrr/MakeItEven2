package com.example.makeiteven2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.makeiteven2.data_models.NameAndScoreInfo
import com.example.makeiteven2.firebase.FireBaseHelper
import com.example.makeiteven2.room.DatabaseHelper

class NameAndScoreInfoViewModel : ViewModel() {
    private var mData: MutableLiveData<ArrayList<NameAndScoreInfo>>? = null

    fun loadList() {
        mData?.value = DatabaseHelper.getCurrentScoreBoard()
        FireBaseHelper.getScoreBoardListFromDataBase(mData!!)
    }


    fun getData(): LiveData<ArrayList<NameAndScoreInfo>>? {
        if (mData == null) {
            mData = MutableLiveData()
        }
        return mData
    }

}