package com.example.learnlanguage.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

private const val TAG_WORK = "OUTPUT"
private const val WORK_NAME = "send_notification_work"

class NotificationRepository(context: Context) {
    private val workManager = WorkManager.getInstance(context)

    fun scheduleNotification() {
        var notifRequest = PeriodicWorkRequestBuilder<NotificationWorker>(8, TimeUnit.HOURS)
            .addTag(TAG_WORK)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            notifRequest
        )
    }

    fun cancelNotification() {
        workManager.cancelUniqueWork(WORK_NAME)
    }
}