package com.micrantha.bluebell.data.download

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

actual class BackgroundDownloadManager(context: Context) {
    companion object {
        const val KEY_URL = "download_url"
        const val KEY_FILE_NAME = "file_name"
        const val KEY_TASK_ID = "task_id"
    }

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

                        downloadListener?.onDownloadProgress(taskId, progress, bytesDownloaded, totalBytes)
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

class DownloadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val downloadService by lazy { DownloadService() }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val url = inputData.getString(BackgroundDownloadManager.KEY_URL)
            ?: return@withContext Result.failure()
        val fileName = inputData.getString(BackgroundDownloadManager.KEY_FILE_NAME)
            ?: return@withContext Result.failure()
        val taskId = inputData.getString(BackgroundDownloadManager.KEY_TASK_ID)
            ?: return@withContext Result.failure()

        try {
            downloadFile(url, fileName, taskId)
        } catch (e: Exception) {
            Result.failure(
                workDataOf("error" to (e.message ?: "Download failed"))
            )
        } finally {
            downloadService.close()
        }
    }

    private suspend fun downloadFile(
        urlString: String,
        fileName: String,
        taskId: String
    ): Result {
        return try {
            val downloadDir = File(applicationContext.filesDir, "downloads")
            if (!downloadDir.exists()) {
                downloadDir.mkdirs()
            }

            val file = File(downloadDir, fileName)
            val resumePosition = if (file.exists()) file.length() else 0L

            if (resumePosition == 0L) {
                try {
                    downloadService.getFileInfo(urlString)
                    // Could update fileName here if needed
                } catch (e: Exception) {
                    // Continue with download even if HEAD request fails
                }
            }

            val fileData = downloadService.downloadFile(
                url = urlString,
                resumePosition = resumePosition
            ) { bytesDownloaded, totalBytes, progress ->
                if (isStopped) return@downloadFile

                setProgress(
                    workDataOf(
                        "progress" to progress,
                        "bytesDownloaded" to bytesDownloaded,
                        "totalBytes" to totalBytes
                    )
                )
            }

            if (resumePosition > 0) {
                FileOutputStream(file, true).use { output ->
                    output.write(fileData)
                }
            } else {
                file.writeBytes(fileData)
            }

            Result.success(
                workDataOf("filePath" to file.absolutePath)
            )

        } catch (e: Exception) {
            Result.failure(
                workDataOf("error" to (e.message ?: "Download failed"))
            )
        }
    }
}
