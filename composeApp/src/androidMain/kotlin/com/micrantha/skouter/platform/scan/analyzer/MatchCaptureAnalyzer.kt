package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.components.containers.Embedding
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.core.RunningMode.LIVE_STREAM
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder.ImageEmbedderOptions
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedderResult
import com.micrantha.bluebell.data.Log
import com.micrantha.skouter.domain.model.MatchClue
import com.micrantha.skouter.domain.model.MatchProof
import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.baseOptions
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import okio.ByteString.Companion.toByteString

private const val MODEL_ASSET = "models/embedding/image.tflite"

typealias EmbeddingAnalyzerConfig = CameraAnalyzerConfig<MatchProof, ImageEmbedderOptions.Builder, ImageEmbedder, ImageEmbedderResult>

actual class MatchCaptureAnalyzer(
    context: Context,
) : MatchAnalyzer(context), CaptureAnalyzer<MatchProof> {

    private val client by lazy {
        super.client { setRunningMode(IMAGE) }
    }

    actual override suspend fun analyze(image: CameraImage): Result<MatchProof> = try {
        val result = client.embed(
            image.asMPImage(), image.processingOptions
        )
        Result.success(super.map(result))
    } catch (err: Throwable) {
        Log.e("analyzer", err) { "unable to match image" }
        Result.failure(err)
    }
}

class MatchStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<MatchProof>,
) : MatchAnalyzer(context), StreamAnalyzer {

    private val client by lazy {
        super.client {
            setResultListener(::onResult)
            setErrorListener(callback::onAnalyzerError)
            setRunningMode(LIVE_STREAM)
        }
    }

    override fun analyze(image: CameraImage) {
        client.embedAsync(
            image.asMPImage(), image.processingOptions, image.timestamp
        )
    }

    private fun onResult(result: ImageEmbedderResult, input: MPImage) {
        callback.onAnalyzerResult(super.map(result))
    }
}

abstract class MatchAnalyzer(private val context: Context) : EmbeddingAnalyzerConfig {
    override fun map(result: ImageEmbedderResult): MatchProof {
        return result.embeddingResult().embeddings().map(::embedding).toList()
    }

    private fun embedding(data: Embedding) = MatchClue(data.quantizedEmbedding().toByteString())

    override fun client(block: ImageEmbedderOptions.Builder.() -> Unit): ImageEmbedder {
        val options = ImageEmbedderOptions.builder()
            .setBaseOptions(baseOptions(MODEL_ASSET))
            .setQuantize(true)
            .apply(block).build()
        return ImageEmbedder.createFromOptions(context, options)
    }
}
