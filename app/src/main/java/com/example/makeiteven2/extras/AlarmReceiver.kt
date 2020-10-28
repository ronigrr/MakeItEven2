package com.example.makeiteven2.extras

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        NotificationHelper(context!!).createNotification(Constants.NotificationText,"")
    }
}