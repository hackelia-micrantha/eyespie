package com.micrantha.eyespie.platform.scan.analyzer

import android.content.Context
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.core.RunningMode.LIVE_STREAM
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifier
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifier.ImageClassifierOptions
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifierResult
import com.micrantha.bluebell.app.Log
import com.micrantha.eyespie.domain.entities.LabelClue
import com.micrantha.eyespie.domain.entities.LabelProof
import com.micrantha.eyespie.platform.scan.CameraAnalyzerConfig
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.AnalyzerCallback
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer
import com.micrantha.eyespie.platform.scan.components.StreamAnalyzer

private const val MODEL_ASSET = "classification_efficientnet.tflite"
private const val MODEL_ASSET_LITE = "classification_efficientnet_lite.tflite"
private const val MODEL_ASSET_AUDIO = "classification_yamnet_audio.tflite"

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
        Log.e("analyzer", err) { "unable to label image" }
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
