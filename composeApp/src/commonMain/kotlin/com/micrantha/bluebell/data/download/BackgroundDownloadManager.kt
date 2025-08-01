package com.micrantha.bluebell.data.download

expect class BackgroundDownloadManager {
    fun startDownload(url: String, fileName: String? = null): String
    fun cancelDownload(taskId: String)
    fun pauseDownload(taskId: String)
    fun resumeDownload(taskId: String)
    fun getDownloadProgress(taskId: String): Float
    fun setDownloadListener(listener: DownloadListener)
    fun cleanup()
}
