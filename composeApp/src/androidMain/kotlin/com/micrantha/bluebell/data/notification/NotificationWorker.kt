package com.micrantha.bluebell.data.notification

import android.Manifest
import android.R
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.micrantha.bluebell.platform.Notifications.Companion.NOTIFICATION_CHANNEL_DEFAULT

class NotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        val id = inputData.getString("id") ?: return Result.failure()
        val title = inputData.getString("title") ?: ""
        val message = inputData.getString("message") ?: ""

        val builder = NotificationCompat.Builder(context, "default")
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setChannelId(NOTIFICATION_CHANNEL_DEFAULT)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(context).notify(id.hashCode(), builder.build())
        return Result.success()
    }
}
