package com.micrantha.bluebell.data.download
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray

class DownloadService(
    private val httpClient: HttpClient = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
            connectTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }
        expectSuccess = false
    }
) {

    suspend fun downloadFile(
        url: String,
        resumePosition: Long = 0L,
        progressCallback: suspend (bytesDownloaded: Long, totalBytes: Long, progress: Float) -> Unit
    ): ByteArray {
        val response = httpClient.get(url) {
            if (resumePosition > 0) {
                header(HttpHeaders.Range, "bytes=$resumePosition-")
            }
        }

        if (!response.status.isSuccess() && response.status != HttpStatusCode.PartialContent) {
            throw Exception("HTTP Error: ${response.status.value} - ${response.status.description}")
        }

        val contentLength = response.headers[HttpHeaders.ContentLength]?.toLongOrNull() ?: -1L
        val totalBytes = if (contentLength > 0) contentLength + resumePosition else -1L

        val channel = response.bodyAsChannel()
        val result = ByteArray(if (contentLength > 0) contentLength.toInt() else 8192 * 1024)
        var bytesRead = 0
        var totalBytesDownloaded = resumePosition

        while (!channel.isClosedForRead) {
            val packet = channel.readRemaining(8192)
            if (packet.exhausted()) break

            val bytes = packet.readByteArray()
            if (bytesRead + bytes.size > result.size) {
                val newResult = ByteArray((result.size * 1.5).toInt().coerceAtLeast(bytesRead + bytes.size))
                result.copyInto(newResult, 0, 0, bytesRead)
                bytes.copyInto(newResult, bytesRead)
            } else {
                bytes.copyInto(result, bytesRead)
            }

            bytesRead += bytes.size
            totalBytesDownloaded += bytes.size

            val progress = if (totalBytes > 0) {
                totalBytesDownloaded.toFloat() / totalBytes.toFloat()
            } else 0f

            progressCallback(totalBytesDownloaded, totalBytes, progress)
        }

        return result.copyOf(bytesRead)
    }

    suspend fun getFileInfo(url: String): FileInfo {
        val response = httpClient.head(url)
        if (!response.status.isSuccess()) {
            throw Exception("HTTP Error: ${response.status.value} - ${response.status.description}")
        }

        val contentLength = response.headers[HttpHeaders.ContentLength]?.toLongOrNull() ?: -1L
        val fileName = response.headers[HttpHeaders.ContentDisposition]
            ?.let { disposition ->
                disposition.substringAfter("filename=", "")
                    .trim('"')
                    .takeIf { it.isNotEmpty() }
            } ?: url.substringAfterLast('/').takeIf { it.isNotEmpty() } ?: "download"

        val acceptsRanges = response.headers[HttpHeaders.AcceptRanges]?.contains("bytes") == true

        return FileInfo(
            fileName = fileName,
            contentLength = contentLength,
            supportsResume = acceptsRanges
        )
    }

    fun close() {
        httpClient.close()
    }
}
