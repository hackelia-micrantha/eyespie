package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.core.RunningMode.LIVE_STREAM
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifier
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifier.ImageClassifierOptions
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifierResult
import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageLabel
import com.micrantha.skouter.platform.scan.model.ImageLabels

private const val MODEL_ASSET = "models/classification/image.tflite"

typealias LabelAnalyzerConfig = CameraAnalyzerConfig<ImageLabels, ImageClassifierOptions.Builder, ImageClassifier, ImageClassifierResult>

private fun config(context: Context): LabelAnalyzerConfig = object : LabelAnalyzerConfig {
    override fun map(result: ImageClassifierResult) = result.classificationResult()
        .classifications().flatMap { it.categories() }.map(::label)

    private fun label(label: Category) = ImageLabel(
        confidence = label.score(),
        data = label.categoryName()
    )

    override fun client(block: ImageClassifierOptions.Builder.() -> Unit): ImageClassifier {
        val options = ImageClassifierOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .setModelAssetPath(MODEL_ASSET)
                    .build()
            )
            .setScoreThreshold(0.6f)
            .apply(block).build()
        return ImageClassifier.createFromOptions(context, options)
    }
}

actual class LabelCaptureAnalyzer(
    context: Context,
    private val config: LabelAnalyzerConfig = config(context)
) : CaptureAnalyzer<ImageLabels> {
    private val client by lazy {
        config.client {
            setRunningMode(IMAGE)
        }
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageLabels> = try {
        val result = client.classify(image.asMPImage())
        Result.success(config.map(result))
    } catch (err: Throwable) {
        Result.failure(err)
    }
}

actual class LabelStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<ImageLabels>,
    private val config: LabelAnalyzerConfig = config(context)
) : StreamAnalyzer {

    private val client by lazy {
        config.client {
            setRunningMode(LIVE_STREAM)
            setResultListener(::onResult)
            setErrorListener(callback::onAnalyzerError)
        }
    }

    actual override fun analyze(image: CameraImage) {
        client.classifyAsync(image.asMPImage(), image.timestamp)
    }

    private fun onResult(result: ImageClassifierResult, input: MPImage) {
        callback.onAnalyzerResult(config.map(result))
    }
}
