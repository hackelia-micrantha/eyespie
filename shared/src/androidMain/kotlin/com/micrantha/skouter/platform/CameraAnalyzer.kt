package com.micrantha.skouter.platform

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Matrix
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext

data class CameraAnalyzerOptions(
    val callback: ImageAnalyzerCallback,
    val image: () -> Bitmap?
)

class CameraAnalyzer(
    private val options: CameraAnalyzerOptions,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
    private val scope: CoroutineScope = CoroutineScope(coroutineContext) + Job()
) : ImageAnalysis.Analyzer {

    private var lastJob: Long = 0

    private val matrix = Matrix()

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {

        if (image.imageInfo.timestamp - lastJob < 500) {
            return
        }
        lastJob = image.imageInfo.timestamp

        scope.launch {

            val uiImage = CameraImage(image.toBitmap())

            options.callback(uiImage)

            image.close()
        }
    }

    private lateinit var bitmapBuffer: Bitmap

    private fun ImageProxy.toBitmap(): Bitmap {
        if (!::bitmapBuffer.isInitialized) {
            bitmapBuffer = Bitmap.createBitmap(width, height, ARGB_8888)
        }
        bitmapBuffer.copyPixelsFromBuffer(planes[0].buffer)

        matrix.reset()
        matrix.postRotate(imageInfo.rotationDegrees.toFloat())
        return Bitmap.createBitmap(
            bitmapBuffer,
            0,
            0,
            width,
            height,
            matrix,
            false
        )
    }

}
