package com.asu1.quizzer.BroadcastReceiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.asu1.resources.NOTIFICATION_CHANNEL_ID
import com.asu1.resources.NOTIFICATION_ID
import com.asu1.resources.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.quizzerresize2)
            .setContentTitle("Music Playback")
            .setContentText("Playing music")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

//        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }
}