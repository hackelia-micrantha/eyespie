package com.micrantha.skouter.platform.scan

import android.graphics.RectF
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mediapipe.tasks.core.BaseOptions
import com.micrantha.bluebell.data.Log
import com.micrantha.skouter.platform.scan.components.CameraScannerDispatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun baseOptions(model: String, block: BaseOptions.Builder.() -> Unit = {}) =
    BaseOptions.builder()
        .setModelAssetPath(model)
        .apply(block)
        .build()

interface CameraAnalyzerConfig<Value, Options, Client, Result> {
    fun map(result: Result): Value

    fun client(block: Options.() -> Unit): Client
}

class CameraAnalyzer(
    private val regionOfInterest: RectF? = null,
    private val callback: CameraScannerDispatch,
    private val scope: CoroutineScope
) : ImageAnalysis.Analyzer {
    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        try {
            val uiImage = CameraImage(
                data = image.image,
                width = image.width,
                height = image.height,
                rotation = image.imageInfo.rotationDegrees,
                timestamp = image.imageInfo.timestamp,
                regionOfInterest = regionOfInterest
            )

            scope.launch {
                callback(uiImage)
                //image.close()
            }
        } catch (err: Throwable) {
            Log.e("analyzer", err) { "unable to analyze camera image" }
        }
    }

}
