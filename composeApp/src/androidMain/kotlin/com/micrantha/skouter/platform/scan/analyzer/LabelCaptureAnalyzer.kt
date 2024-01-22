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
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer

private const val MODEL_ASSET = "models/classification/image.tflite"

typealias LabelAnalyzerConfig = CameraAnalyzerConfig<LabelProof, ImageClassifierOptions.Builder, ImageClassifier, ImageClassifierResult>

actual class LabelCaptureAnalyzer(
    context: Context,
) : LabelAnalyzer(context), CaptureAnalyzer<LabelProof> {

    private val client by lazy {
        super.client {
            setRunningMode(IMAGE)
        }
    }

    actual override suspend fun analyze(image: CameraImage): Result<LabelProof> = try {
        val result = client.classify(
            image.asMPImage(), image.processingOptions
        )
        Result.success(super.map(result))
    } catch (err: Throwable) {
        Result.failure(err)
    }
}

class LabelStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<LabelProof>,
) : LabelAnalyzer(context), StreamAnalyzer {

    private val client by lazy {
        super.client {
            setRunningMode(LIVE_STREAM)
            setResultListener(::onResult)
            setErrorListener(callback::onAnalyzerError)
        }
    }

    override fun analyze(image: CameraImage) {
        client.classifyAsync(
            image.asMPImage(),
            image.processingOptions,
            image.timestamp
        )
    }

    private fun onResult(result: ImageClassifierResult, input: MPImage) {
        callback.onAnalyzerResult(super.map(result))
    }
}

abstract class LabelAnalyzer(private val context: Context) : LabelAnalyzerConfig {
    override fun map(result: ImageClassifierResult) = result.classificationResult()
        .classifications().flatMap { it.categories() }.map(::label).toSet()

    private fun label(label: Category) = LabelClue(
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
