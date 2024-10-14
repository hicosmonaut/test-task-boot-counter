package com.test.task.bootcounter.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.test.task.bootcounter.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationService : Service() {

    private val notificationId = 111

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(
            notificationId,
            createNotification(intent)
        )

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "boot_channel",
                "Boot notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(intent: Intent): Notification {
        val notificationIntent =
            Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val content = intent.getStringExtra("content")

        return NotificationCompat.Builder(this, "boot_channel")
            .setContentTitle("Synchronizing data")
            .setContentText(content)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()
    }
}