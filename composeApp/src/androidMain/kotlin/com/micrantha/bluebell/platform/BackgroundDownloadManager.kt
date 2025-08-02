package com.micrantha.bluebell.platform

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.micrantha.bluebell.data.download.DownloadListener
import com.micrantha.bluebell.data.download.DownloadService
import com.micrantha.bluebell.data.download.DownloadStatus
import com.micrantha.bluebell.data.download.DownloadTask
import com.micrantha.bluebell.data.download.DownloadWorker
import com.micrantha.bluebell.data.download.KEY_FILE_NAME
import com.micrantha.bluebell.data.download.KEY_TASK_ID
import com.micrantha.bluebell.data.download.KEY_URL
import java.util.UUID

actual class BackgroundDownloadManager(context: Context) {

    private var downloadListener: DownloadListener? = null
    private val downloadTasks = mutableMapOf<String, DownloadTask>()
    private val workManager = WorkManager.getInstance(context)
    private val downloadService = DownloadService()

    actual fun startDownload(url: String, fileName: String?): String {
        val taskId = UUID.randomUUID().toString()

        val task = DownloadTask(
            taskId = taskId,
            url = url,
            fileName = fileName ?: "download_$taskId",
            status = DownloadStatus.PENDING
        )

        downloadTasks[taskId] = task

        val downloadData = workDataOf(
            KEY_URL to url,
            KEY_FILE_NAME to task.fileName,
            KEY_TASK_ID to taskId
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setInputData(downloadData)
            .setConstraints(constraints)
            .addTag(taskId)
            .build()

        workManager.enqueue(downloadRequest)
        observeWorkProgress(taskId)
        downloadListener?.onDownloadStarted(taskId, task.fileName)

        return taskId
    }

    actual fun cancelDownload(taskId: String) {
        workManager.cancelAllWorkByTag(taskId)
        downloadTasks[taskId] = downloadTasks[taskId]?.copy(status = DownloadStatus.CANCELLED)
            ?: return
    }

    actual fun pauseDownload(taskId: String) {
        workManager.cancelAllWorkByTag(taskId)
        downloadTasks[taskId] = downloadTasks[taskId]?.copy(status = DownloadStatus.PAUSED)
            ?: return
        downloadListener?.onDownloadPaused(taskId)
    }

    actual fun resumeDownload(taskId: String) {
        val task = downloadTasks[taskId] ?: return
        if (task.status == DownloadStatus.PAUSED) {
            val downloadData = workDataOf(
                KEY_URL to task.url,
                KEY_FILE_NAME to task.fileName,
                KEY_TASK_ID to taskId
            )

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
                .setInputData(downloadData)
                .setConstraints(constraints)
                .addTag(taskId)
                .build()

            workManager.enqueue(downloadRequest)
            observeWorkProgress(taskId)

            downloadTasks[taskId] = task.copy(status = DownloadStatus.DOWNLOADING)
            downloadListener?.onDownloadResumed(taskId)
        }
    }

    actual fun getDownloadProgress(taskId: String): Float {
        return downloadTasks[taskId]?.progress ?: 0f
    }

    actual fun setDownloadListener(listener: DownloadListener) {
        this.downloadListener = listener
    }

    actual fun cleanup() {
        downloadService.close()
    }

    private fun observeWorkProgress(taskId: String) {
        workManager.getWorkInfosByTagLiveData(taskId).observeForever { workInfos ->
            workInfos?.firstOrNull()?.let { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.RUNNING -> {
                        val progress = workInfo.progress.getFloat("progress", 0f)
                        val bytesDownloaded = workInfo.progress.getLong("bytesDownloaded", 0L)
                        val totalBytes = workInfo.progress.getLong("totalBytes", 0L)

                        downloadTasks[taskId] = downloadTasks[taskId]?.copy(
                            status = DownloadStatus.DOWNLOADING,
                            progress = progress,
                            bytesDownloaded = bytesDownloaded,
                            totalBytes = totalBytes
                        ) ?: return@let

                        downloadListener?.onDownloadProgress(
                            taskId,
                            progress,
                            bytesDownloaded,
                            totalBytes
                        )
                    }

                    WorkInfo.State.SUCCEEDED -> {
                        val filePath = workInfo.outputData.getString("filePath") ?: ""
                        downloadTasks[taskId] = downloadTasks[taskId]?.copy(
                            status = DownloadStatus.COMPLETED,
                            progress = 1.0f
                        ) ?: return@let

                        downloadListener?.onDownloadCompleted(taskId, filePath)
                    }

                    WorkInfo.State.FAILED -> {
                        val error = workInfo.outputData.getString("error") ?: "Unknown error"
                        downloadTasks[taskId] = downloadTasks[taskId]?.copy(
                            status = DownloadStatus.FAILED
                        ) ?: return@let

                        downloadListener?.onDownloadFailed(taskId, error)
                    }

                    WorkInfo.State.CANCELLED -> {
                        downloadTasks[taskId] = downloadTasks[taskId]?.copy(
                            status = DownloadStatus.CANCELLED
                        ) ?: return@let
                    }

                    else -> {}
                }
            }
        }
    }
}