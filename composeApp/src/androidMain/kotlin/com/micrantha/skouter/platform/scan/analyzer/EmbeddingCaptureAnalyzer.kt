package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.components.containers.Embedding
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.core.RunningMode.LIVE_STREAM
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder.ImageEmbedderOptions
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedderResult
import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.baseOptions
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageEmbeddings
import okio.ByteString.Companion.toByteString

private const val MODEL_ASSET = "models/embeddings/image.tflite"

typealias EmbeddingAnalyzerConfig = CameraAnalyzerConfig<ImageEmbeddings, ImageEmbedderOptions.Builder, ImageEmbedder, ImageEmbedderResult>

actual class EmbeddingCaptureAnalyzer(
    context: Context,
    private val config: EmbeddingAnalyzerConfig = config(context)
) : CaptureAnalyzer<ImageEmbeddings> {

    private val client by lazy {
        config.client { setRunningMode(IMAGE) }
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageEmbeddings> = try {
        val result = client.embed(image.asMPImage())
        Result.success(config.map(result))
    } catch (err: Throwable) {
        Result.failure(err)
    }
}

actual class EmbeddingStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<ImageEmbeddings>,
    private val config: EmbeddingAnalyzerConfig = config(context)
) : StreamAnalyzer {
    private val client by lazy {
        config.client {
            setResultListener(::onResult)
            setErrorListener(callback::onAnalyzerError)
            setRunningMode(LIVE_STREAM)
        }
    }

    actual override fun analyze(image: CameraImage) {
        client.embedAsync(image.asMPImage(), image.timestamp)
    }

    private fun onResult(result: ImageEmbedderResult, input: MPImage) {
        callback.onAnalyzerResult(config.map(result))
    }
}

private fun config(context: Context): EmbeddingAnalyzerConfig = object : EmbeddingAnalyzerConfig {
    override fun map(result: ImageEmbedderResult): ImageEmbeddings {
        return result.embeddingResult().embeddings().map(::embedding)
    }

    private fun embedding(data: Embedding) = data.quantizedEmbedding().toByteString()

    override fun client(block: ImageEmbedderOptions.Builder.() -> Unit): ImageEmbedder {
        val options = ImageEmbedderOptions.builder()
            .setBaseOptions(baseOptions(MODEL_ASSET))
            .setQuantize(true)
            .apply(block).build()
        return ImageEmbedder.createFromOptions(context, options)
    }
}
