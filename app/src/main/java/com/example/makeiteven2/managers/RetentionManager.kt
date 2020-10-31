package com.example.makeiteven2.managers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.makeiteven2.extras.AlarmReceiver
import com.example.makeiteven2.extras.SingletonHolder

class RetentionManager private constructor(context: Context) {
    private val mIntent: Intent = Intent(context, AlarmReceiver::class.java)
    private val mPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT)
    private val mAlarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setNotification(intervalInMillis: Long) {
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalInMillis, mPendingIntent)
    }

    fun cancelNotification() {
        mAlarmManager.cancel(mPendingIntent)
    }

    companion object : SingletonHolder<RetentionManager, Context>(::RetentionManager)
}