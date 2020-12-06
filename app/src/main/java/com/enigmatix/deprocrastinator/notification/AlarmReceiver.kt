package com.enigmatix.deprocrastinator.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.enigmatix.deprocrastinator.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val id = intent.getIntExtra("id", 0)
        val builder = Notification.Builder(context, CHANNEL_ID)
        val notification: Notification = builder.setContentTitle(title)
            .setContentText(description)
            .setStyle(Notification.BigTextStyle().bigText(description))
            .setTicker("You got a subtask to take care of!")
            .setSmallIcon(R.drawable.icon)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Subtask Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(id, notification)
    }

    companion object {
        private const val CHANNEL_ID = "com.enigmatix.deprocrastinator.channelId"
    }
}