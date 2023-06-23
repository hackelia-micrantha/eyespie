package com.micrantha.skouter.platform

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.os.Looper
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

data class CameraAnalyzerOptions(
    val callback: (CameraImage) -> Unit,
    val image: () -> Bitmap?
)

class CameraAnalyzer(
    private val options: CameraAnalyzerOptions
) : ImageAnalysis.Analyzer {

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        val rotation = image.imageInfo.rotationDegrees

        val bitmap = options.image() ?: image.toBitmap()

        val cameraImage = CameraImage(bitmap, rotation)

        options.callback(cameraImage)

        image.close()

        if (Looper.getMainLooper().isCurrentThread.not()) {
            Thread.sleep(250)
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
