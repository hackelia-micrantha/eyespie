package com.micrantha.skouter.platform

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext

data class CameraAnalyzerOptions(
    val callback: (CameraImage) -> Unit,
    val image: () -> Bitmap?
)

class CameraAnalyzer(
    private val options: CameraAnalyzerOptions,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
    private val scope: CoroutineScope = CoroutineScope(coroutineContext) + Job()
) : ImageAnalysis.Analyzer {

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        scope.launch {
            val bitmap = image.toBitmap()

            val cameraImage = CameraImage(bitmap, image.imageInfo.rotationDegrees)

            options.callback(cameraImage)

            delay(500)

            image.close()
        }
    }

    private lateinit var bitmapBuffer: Bitmap

    private fun ImageProxy.toBitmap(): Bitmap {
        if (!::bitmapBuffer.isInitialized) {
            bitmapBuffer = Bitmap.createBitmap(width, height, ARGB_8888)
        }
        bitmapBuffer.copyPixelsFromBuffer(planes[0].buffer)

        return bitmapBuffer
    }

}
