package com.example.learnlanguage.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.learnlanguage.R

class NotificationWorker(
    ctx: Context,
    params:WorkerParameters
): Worker(ctx, params) {

    override fun doWork(): Result {
        makeNotification(applicationContext)
        return Result.success()
    }

    fun makeNotification(context: Context) {

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = "Learn language notification channel"
            val description = "sends notification periodically"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("LearnLanguage Notification", name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(context, "LearnLanguage Notification")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Learn Language")
            .setContentText("Practice makes better")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        // Show the notification
        NotificationManagerCompat.from(context).notify(1, builder.build())
    }


}