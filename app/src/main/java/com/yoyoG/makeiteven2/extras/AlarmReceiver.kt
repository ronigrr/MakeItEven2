package com.yoyoG.makeiteven2.extras

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yoyoG.makeiteven2.managers.RetentionManager

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        NotificationHelper(context!!).createNotification(Constants.NOTIFICATION_TEST, "")
        RetentionManager.getInstance(context).setNotification(Constants.NOTIFICATION_COUNTDOWN_5_DAYS_IN_MILLIS)
    }
}