package com.micrantha.skouter.platform

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.arch.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext

data class CameraAnalyzerOptions(
    val dispatch: Dispatch,
    val image: () -> Bitmap?
)

class CameraAnalyzer(
    private val options: CameraAnalyzerOptions,
    context: CoroutineContext = Dispatchers.Default,
) : ImageAnalysis.Analyzer, Dispatcher {

    private val scope = CoroutineScope(context) + Job()

    override fun dispatch(action: Action) {
        options.dispatch(action)
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        scope.launch {
            val rotation = image.imageInfo.rotationDegrees

            val bitmap = image.use { it.toBitmap() }

            val cameraImage = CameraImage(bitmap, rotation)

            dispatch(ImageCaptured(cameraImage))
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
