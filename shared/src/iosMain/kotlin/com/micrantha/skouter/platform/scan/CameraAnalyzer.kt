package com.micrantha.skouter.platform.scan

import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.Foundation.NSError
import platform.Vision.VNImageRequestHandler
import platform.Vision.VNRequest
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

abstract class CameraAnalyzer<out T, R : VNRequest> : CaptureAnalyzer<T> {
    abstract suspend fun map(response: List<*>): T

    abstract fun request(): R

    override suspend fun analyze(image: CameraImage): Result<T> = try {
        val cgImage = image.asCGImage()

        val imageRequestHandler =
            VNImageRequestHandler(cgImage, image.orientation, emptyMap<Any?, String>())

        val result = suspendCoroutine { continuation ->
            execute(imageRequestHandler, onError = {
                continuation.resumeWithException(it)
            }) {
                continuation.resume(it)
            }
        }

        Result.success(map(result.results!!))
    } catch (err: Throwable) {
        Result.failure(err)
    }

    private fun execute(
        handler: VNImageRequestHandler,
        onError: (Throwable) -> Unit,
        onSuccess: (R) -> Unit
    ) {

        dispatch_async(dispatch_get_global_queue(0, 0)) {
            try {
                val err = memScoped {
                    alloc<ObjCObjectVar<NSError?>>()
                }
                val req = request()

                handler.performRequests(listOf(req), err.ptr)

                onSuccess(req)
            } catch (err: Throwable) {
                onError(err)
            }
        }
    }
}
