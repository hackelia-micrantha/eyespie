package com.micrantha.bluebell.data.download

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

internal const val KEY_URL = "download_url"
internal const val KEY_FILE_NAME = "file_name"
internal const val KEY_TASK_ID = "task_id"

class DownloadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val downloadService by lazy { DownloadService() }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val url = inputData.getString(KEY_URL)
            ?: return@withContext Result.failure()
        val fileName = inputData.getString(KEY_FILE_NAME)
            ?: return@withContext Result.failure()
        val taskId = inputData.getString(KEY_TASK_ID)
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
