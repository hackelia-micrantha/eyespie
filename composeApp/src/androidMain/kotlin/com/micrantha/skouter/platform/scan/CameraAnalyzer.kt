package com.micrantha.skouter.platform.scan

import android.graphics.Bitmap
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.micrantha.skouter.platform.scan.components.CameraScannerDispatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext

class CameraAnalyzer(
    private val callback: CameraScannerDispatch,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
    private val scope: CoroutineScope = CoroutineScope(coroutineContext) + Job()
) : ImageAnalysis.Analyzer {

    private var lastJob: Long = 0

    private lateinit var bitmapBuffer: Bitmap

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {

        if (image.imageInfo.timestamp - lastJob < 100) {
            return
        }

        lastJob = image.imageInfo.timestamp

        scope.launch {

            val uiImage = CameraImage(
                processImage(image),
                rotation = image.imageInfo.rotationDegrees,
                lastJob
            )

            callback(uiImage)

            image.close()
        }
    }

    private fun processImage(image: ImageProxy): Bitmap {
        if (!::bitmapBuffer.isInitialized) {
            bitmapBuffer = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        }

        image.planes[0].buffer.apply {
            rewind()
            bitmapBuffer.copyPixelsFromBuffer(this)
        }

        return bitmapBuffer
    }
}
