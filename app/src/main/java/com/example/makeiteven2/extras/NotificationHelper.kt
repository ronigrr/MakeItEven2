package com.example.makeiteven2.extras

import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.makeiteven2.MainActivity


class NotificationHelper(private val mContext: Context) {

    private var mBuilder: NotificationCompat.Builder? = null
    private var mChannelId: String? = null
    private var mNotificationManagerCompat: NotificationManagerCompat? = null



    @SuppressLint("RestrictedApi")
    fun createNotification(textToShow: String?, channelId: String) {
        createChannelId(channelId)
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        mBuilder = NotificationCompat.Builder(mContext, mChannelId!!)
        mBuilder?.apply {
            setSmallIcon(R.drawable.star_on)
            setLargeIcon(BitmapFactory.decodeResource(mContext.resources,com.example.makeiteven2.R.drawable.ic_app_round))
            setContentIntent(PendingIntent.getActivity(mContext,0, Intent(mContext,MainActivity::class.java),PendingIntent.FLAG_CANCEL_CURRENT))
            setContentText(textToShow)
            setStyle(NotificationCompat.BigTextStyle().bigText(textToShow))
            setAutoCancel(true)
            setChannelId(mChannelId!!)
            setSound(defaultSoundUri) }
        mBuilder?.build()?.let { mNotificationManagerCompat!!.notify(1, it) }
    }

    private fun createChannelId(channelId: String) {
        mChannelId = channelId
        mNotificationManagerCompat = NotificationManagerCompat.from(mContext)
        val mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(mChannelId, "CHANNEL", NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            mNotificationManager.createNotificationChannel(channel)
        }
    }
}