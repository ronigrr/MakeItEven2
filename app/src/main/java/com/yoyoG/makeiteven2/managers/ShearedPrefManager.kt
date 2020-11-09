package com.yoyoG.makeiteven2.managers

import android.content.Context
import android.content.SharedPreferences
import com.yoyoG.makeiteven2.extras.Constants

object ShearedPrefManager {

    private lateinit var mSharedPref: SharedPreferences

    private fun getDataBase(applicationContext: Context) {
        mSharedPref = applicationContext.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
    }

    fun getIsFirstTimeInApp(context: Context): Boolean {
        getDataBase(context)
        return mSharedPref.getBoolean(Constants.IS_FIRST_TIME_IN_APP, true)
    }

    fun setIsFirstTimeInApp(context: Context, boolean: Boolean) {
        getDataBase(context)
        mSharedPref.edit().putBoolean(Constants.IS_FIRST_TIME_IN_APP, boolean).apply()
    }

    fun getIsFirstTimeInStageMode(context: Context): Boolean {
        getDataBase(context)
        return mSharedPref.getBoolean(Constants.IS_FIRST_TIME_IN_STAGE_MODE, java.lang.Boolean.TRUE)
    }

    fun setIsFirstTimeInStageMode(context: Context, boolean: Boolean) {
        getDataBase(context)
        mSharedPref.edit().putBoolean(Constants.IS_FIRST_TIME_IN_STAGE_MODE, boolean).apply()
    }
}