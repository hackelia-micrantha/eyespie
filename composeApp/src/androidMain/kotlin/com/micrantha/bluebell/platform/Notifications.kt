package com.micrantha.bluebell.platform

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationManagerCompat
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.micrantha.bluebell.app.LocalNotifier
import com.micrantha.bluebell.data.notification.NotificationWorker
import java.util.concurrent.TimeUnit

actual class Notifications(
    private val context: Context
) : LocalNotifier {

     init {
         WorkManager.initialize(
             context,
             Configuration.Builder().build()
         )
        createChannel(context)
    }

    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
            "default",
            "Default Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    actual override fun schedule(id: String, title: String, message: String, atMillis: Long?) {
        val delay = atMillis?.let {
            it - System.currentTimeMillis()
        } ?: 0L

        val data = workDataOf(
            "id" to id,
            "title" to title,
            "message" to message
        )

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(data)
            .setInitialDelay(delay.coerceAtLeast(0), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            id,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    actual override fun cancel(id: String) {
        WorkManager.getInstance(context).cancelUniqueWork(id)
        NotificationManagerCompat.from(context).cancel(id.hashCode())
    }
}
