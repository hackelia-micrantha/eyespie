package com.micrantha.bluebell.data.download

data class DownloadTask(
    val taskId: String,
    val url: String,
    val fileName: String,
    val status: DownloadStatus,
    val progress: Float = 0f,
    val bytesDownloaded: Long = 0L,
    val totalBytes: Long = 0L
)

enum class DownloadStatus {
    PENDING,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED,
    CANCELLED
}

data class FileInfo(
    val fileName: String,
    val contentLength: Long,
    val supportsResume: Boolean
)

interface DownloadListener {
    fun onDownloadStarted(taskId: String, fileName: String)
    fun onDownloadProgress(taskId: String, progress: Float, bytesDownloaded: Long, totalBytes: Long)
    fun onDownloadCompleted(taskId: String, filePath: String)
    fun onDownloadFailed(taskId: String, error: String)
    fun onDownloadPaused(taskId: String)
    fun onDownloadResumed(taskId: String)
}
