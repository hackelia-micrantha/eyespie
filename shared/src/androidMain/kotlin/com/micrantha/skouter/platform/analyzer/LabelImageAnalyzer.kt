package com.micrantha.skouter.platform.analyzer

import android.content.Context
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifier
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifier.ImageClassifierOptions
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageLabel
import com.micrantha.skouter.platform.ImageLabels

private const val MODEL_ASSET = "models/classification/image.tflite"

actual class LabelImageAnalyzer(context: Context) : ImageAnalyzer<ImageLabels> {

    private val client by lazy {
        val options = ImageClassifierOptions.builder()
            .setRunningMode(IMAGE)
            .setBaseOptions(
                BaseOptions.builder()
                    .setModelAssetPath(MODEL_ASSET)
                    .build()
            )
            .setScoreThreshold(0.6f)
            .build()
        ImageClassifier.createFromOptions(context, options)
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageLabels> = try {
        val input = BitmapImageBuilder(image.bitmap).build()

        val result = client.classify(input)
            .classificationResult().classifications().flatMap { it.categories() }
            .map(::map)

        Result.success(result)
    } catch (err: Throwable) {
        Result.failure(err)
    }
        

    private fun map(label: Category) = ImageLabel(
        confidence = label.score(),
        data = label.categoryName()
    )
}
