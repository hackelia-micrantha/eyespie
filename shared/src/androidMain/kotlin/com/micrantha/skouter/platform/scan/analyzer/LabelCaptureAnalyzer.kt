package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.core.RunningMode.LIVE_STREAM
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifier
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifier.ImageClassifierOptions
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifierResult
import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageLabel
import com.micrantha.skouter.platform.scan.model.ImageLabels

private const val MODEL_ASSET = "models/classification/image.tflite"

actual class LabelCaptureAnalyzer(
    context: Context,
) : CaptureAnalyzer<ImageLabels> {
    private val client by lazy {
        baseOptions().setRunningMode(IMAGE).client(context)
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageLabels> = try {
        val input = BitmapImageBuilder(image.bitmap).build()
        client.classify(input).analyze()
    } catch (err: Throwable) {
        Result.failure(err)
    }
}

actual class LabelStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<ImageLabels>
) : StreamAnalyzer {

    private val client by lazy {
        baseOptions()
            .setRunningMode(LIVE_STREAM)
            .setResultListener(::onResult)
            .setErrorListener(callback::onAnalyzerError)
            .client(context)
    }

    actual override fun analyze(image: CameraImage) {
        val input = BitmapImageBuilder(image.bitmap).build()
        client.classifyAsync(input, image.timestamp)
    }

    private fun onResult(result: ImageClassifierResult, input: MPImage) {
        result.analyze()
            .onSuccess(callback::onAnalyzerResult)
            .onFailure(callback::onAnalyzerError)
    }
}

private fun map(label: Category) = ImageLabel(
    confidence = label.score(),
    data = label.categoryName()
)


private fun ImageClassifierResult.analyze(): Result<ImageLabels> {

    val labels = classificationResult()
        .classifications()
        .flatMap { it.categories() }
        .map(::map)

    return Result.success(labels)
}

private fun baseOptions() = ImageClassifierOptions.builder()
    .setBaseOptions(
        BaseOptions.builder()
            .setModelAssetPath(MODEL_ASSET)
            .build()
    )
    .setScoreThreshold(0.6f)

private fun ImageClassifierOptions.Builder.client(context: Context) = let {
    ImageClassifier.createFromOptions(context, it.build())
}
