import com.micrantha.bluebell.data.download.DownloadListener
import com.micrantha.bluebell.data.download.DownloadTask
import com.micrantha.bluebell.data.download.DownloadStatus
import com.micrantha.bluebell.data.download.DownloadService
import platform.Foundation.*
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import platform.darwin.NSObject
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
actual class BackgroundDownloadManager {
    companion object {
        const val BACKGROUND_SESSION_ID = "com.app.background.downloads"
    }

    private var downloadListener: DownloadListener? = null
    private val downloadTasks = mutableMapOf<String, DownloadTask>()
    private val urlSessionTasks = mutableMapOf<String, NSURLSessionDownloadTask>()
    private val downloadService = DownloadService()
    private val downloadJobs = mutableMapOf<String, Job>()
    private val downloadScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val backgroundSession: NSURLSession by lazy {
        val config = NSURLSessionConfiguration.backgroundSessionConfigurationWithIdentifier(
            BACKGROUND_SESSION_ID
        )
        config.allowsCellularAccess = true
        config.discretionary = false
        config.sessionSendsLaunchEvents = true

        NSURLSession.sessionWithConfiguration(
            configuration = config,
            delegate = downloadSessionDelegate,
            delegateQueue = NSOperationQueue.mainQueue
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private val downloadSessionDelegate = object : NSObject(), NSURLSessionDownloadDelegateProtocol {

        override fun URLSession(
            session: NSURLSession,
            downloadTask: NSURLSessionDownloadTask,
            didFinishDownloadingToURL: NSURL
        ) {
            val taskId = downloadTask.taskIdentifier.toString()
            val task = downloadTasks[taskId] ?: return

            val documentsPath = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            ).firstOrNull() as? String ?: return

            val destinationPath = "$documentsPath/${task.fileName}"
            val destinationURL = NSURL.fileURLWithPath(destinationPath)

            val fileManager = NSFileManager.defaultManager

            if (fileManager.fileExistsAtPath(destinationPath)) {
                fileManager.removeItemAtURL(destinationURL, error = null)
            }

            val success = fileManager.moveItemAtURL(
                didFinishDownloadingToURL,
                destinationURL,
                error = null
            )

            if (success) {
                downloadTasks[taskId] = task.copy(
                    status = DownloadStatus.COMPLETED,
                    progress = 1.0f
                )
                downloadListener?.onDownloadCompleted(taskId, destinationPath)
            } else {
                downloadTasks[taskId] = task.copy(status = DownloadStatus.FAILED)
                downloadListener?.onDownloadFailed(taskId, "Failed to move file to destination")
            }

            urlSessionTasks.remove(taskId)
        }

        override fun URLSession(
            session: NSURLSession,
            downloadTask: NSURLSessionDownloadTask,
            didWriteData: Long,
            totalBytesWritten: Long,
            totalBytesExpectedToWrite: Long
        ) {
            val taskId = downloadTask.taskIdentifier.toString()
            val task = downloadTasks[taskId] ?: return

            val progress = if (totalBytesExpectedToWrite > 0) {
                totalBytesWritten.toFloat() / totalBytesExpectedToWrite.toFloat()
            } else 0f

            downloadTasks[taskId] = task.copy(
                progress = progress,
                bytesDownloaded = totalBytesWritten,
                totalBytes = totalBytesExpectedToWrite,
                status = DownloadStatus.DOWNLOADING
            )

            downloadListener?.onDownloadProgress(
                taskId,
                progress,
                totalBytesWritten,
                totalBytesExpectedToWrite
            )
        }

        override fun URLSession(
            session: NSURLSession,
            task: NSURLSessionTask,
            didCompleteWithError: NSError?
        ) {
            val taskId = task.taskIdentifier.toString()
            val downloadTask = downloadTasks[taskId] ?: return

            didCompleteWithError?.let { error ->
                downloadTasks[taskId] = downloadTask.copy(status = DownloadStatus.FAILED)
                downloadListener?.onDownloadFailed(taskId, error.localizedDescription)
                urlSessionTasks.remove(taskId)
            }
        }
    }

    actual fun startDownload(url: String, fileName: String? = null): String {
        val taskId = NSUUID().UUIDString
        val actualFileName = fileName ?: "download_$taskId"

        val task = DownloadTask(
            taskId = taskId,
            url = url,
            fileName = actualFileName,
            status = DownloadStatus.PENDING
        )

        downloadTasks[taskId] = task

        downloadScope.launch {
            try {
                if (fileName == null) {
                    val fileInfo = downloadService.getFileInfo(url)
                    downloadTasks[taskId] = task.copy(fileName = fileInfo.fileName)
                }
            } catch (e: Exception) {
                // Continue with default filename if file info fails
            }

            val nsUrl = NSURL.URLWithString(url)
                ?: throw IllegalArgumentException("Invalid URL: $url")

            val downloadTask = backgroundSession.downloadTaskWithURL(nsUrl)
            urlSessionTasks[taskId] = downloadTask
            downloadTask.resume()

            downloadListener?.onDownloadStarted(taskId, actualFileName)
        }

        return taskId
    }

    actual fun cancelDownload(taskId: String) {
        urlSessionTasks[taskId]?.let { task ->
            task.cancel()
            urlSessionTasks.remove(taskId)
        }

        downloadJobs[taskId]?.cancel()
        downloadJobs.remove(taskId)

        downloadTasks[taskId] = downloadTasks[taskId]?.copy(status = DownloadStatus.CANCELLED)
            ?: return
    }

    actual fun pauseDownload(taskId: String) {
        urlSessionTasks[taskId]?.let { task ->
            task.suspend()
            downloadTasks[taskId] = downloadTasks[taskId]?.copy(status = DownloadStatus.PAUSED)
                ?: return
            downloadListener?.onDownloadPaused(taskId)
        }

        downloadJobs[taskId]?.cancel()
        downloadJobs.remove(taskId)
    }

    actual fun resumeDownload(taskId: String) {
        urlSessionTasks[taskId]?.let { task ->
            task.resume()
            downloadTasks[taskId] = downloadTasks[taskId]?.copy(status = DownloadStatus.DOWNLOADING)
                ?: return
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
        downloadScope.cancel()
        downloadJobs.clear()
        downloadService.close()
    }

    fun handleBackgroundSessionEvents(identifier: String, completionHandler: () -> Unit) {
        if (identifier == BACKGROUND_SESSION_ID) {
            backgroundSession.getTasksWithCompletionHandler { dataTasks, uploadTasks, downloadTasks ->
                completionHandler()
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toNSData(): NSData {
    return this.usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = this.size.toULong())
    }
}
