package com.micrantha.skouter.platform.scan

import com.micrantha.skouter.platform.asException
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError
import platform.Vision.VNImageRequestHandler
import platform.Vision.VNObservation
import platform.Vision.VNRequest
import platform.darwin.dispatch_async
import platform.darwin.dispatch_queue_create
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface CameraAnalyzerConfig<out T, out R : VNRequest, O : VNObservation> {
    fun request(): R
    fun map(response: List<O>, image: CameraImage): T

    val filter: (List<*>?) -> List<O>
}

abstract class CameraCaptureAnalyzer<T, R : VNRequest, O : VNObservation>(
    private val config: CameraAnalyzerConfig<T, R, O>
) : CaptureAnalyzer<T>, StreamAnalyzer<T>, CameraAnalyzerConfig<T, R, O> by config {

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun analyzeCapture(image: CameraImage): Result<T> = try {
        val imageRequestHandler =
            VNImageRequestHandler(image.asCGImage(), image.orientation, emptyMap<Any?, String>())

        val result = suspendCoroutine { continuation ->
            imageRequestHandler.execute(request(), onError = {
                continuation.resumeWithException(it)
            }) {
                continuation.resume(it)
            }
        }

        Result.success(map(filter(result.results), image))
    } catch (err: Throwable) {
        Result.failure(err)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun analyzeStream(image: CameraImage, callback: AnalyzerCallback<T>) {
        val imageRequestHandler =
            VNImageRequestHandler(image.asCGImage(), image.orientation, emptyMap<Any?, String>())

        imageRequestHandler.execute(request(), onError = {
            callback.onAnalyzerError(it)
        }) {
            callback.onAnalyzerResult(map(filter(it.results), image))
        }
    }
}

private val executeQueue by lazy {
    dispatch_queue_create(label = "executeImageRequest", null)
}

@OptIn(ExperimentalForeignApi::class)
private fun <T : VNRequest> VNImageRequestHandler.execute(
    request: T,
    onError: (Throwable) -> Unit,
    onSuccess: (T) -> Unit
) {

    dispatch_async(executeQueue) {
        try {
            val err = memScoped {
                alloc<ObjCObjectVar<NSError?>>()
            }
            performRequests(listOf(request), err.ptr)

            err.value?.let {
                throw it.asException()
            }

            onSuccess(request)
        } catch (err: Throwable) {
            onError(err)
        }
    }
}
