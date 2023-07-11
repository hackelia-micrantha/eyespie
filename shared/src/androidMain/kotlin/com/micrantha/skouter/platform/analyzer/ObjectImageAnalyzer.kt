package com.micrantha.skouter.platform.analyzer

import android.content.Context
import androidx.compose.ui.graphics.toComposeRect
import androidx.core.graphics.toRect
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.components.containers.Detection
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector.ObjectDetectorOptions
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageLabel
import com.micrantha.skouter.platform.ImageObject
import com.micrantha.skouter.platform.ImageObjects

private const val MODEL_ASSET = "models/detection/image.tflite"

actual class ObjectImageAnalyzer(context: Context) : ImageAnalyzer<ImageObjects> {
    private val client by lazy {
        val options = ObjectDetectorOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .setModelAssetPath(MODEL_ASSET)
                    .build()
            )
            .setRunningMode(IMAGE)
            .setScoreThreshold(0.7f)
            .setMaxResults(5)
            .build()
        ObjectDetector.createFromOptions(context, options)
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageObjects> = try {
        val input = BitmapImageBuilder(image.bitmap).build()

        val result = client.detect(input).detections().map(::map)

        Result.success(result)
    } catch (err: Throwable) {
        Result.failure(err)
    }

    private fun map(obj: Detection) = ImageObject(
        labels = obj.categories().map { ImageLabel(it.categoryName(), it.score()) },
        rect = obj.boundingBox().toRect().toComposeRect(),
    )
}
