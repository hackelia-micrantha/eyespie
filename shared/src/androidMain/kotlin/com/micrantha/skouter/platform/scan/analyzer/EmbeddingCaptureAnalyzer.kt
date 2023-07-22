package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.components.containers.Embedding
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.core.RunningMode.LIVE_STREAM
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder.ImageEmbedderOptions
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedderResult
import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageEmbeddings
import okio.ByteString.Companion.toByteString

private const val MODEL_ASSET = "models/embeddings/image.tflite"

actual class EmbeddingCaptureAnalyzer(
    context: Context,
) : CaptureAnalyzer<ImageEmbeddings> {

    private val client by lazy {
        baseOptions().setRunningMode(IMAGE).client(context)
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageEmbeddings> = try {
        val input = BitmapImageBuilder(image.bitmap).build()
        client.embed(input).analyze()
    } catch (err: Throwable) {
        Result.failure(err)
    }
}

actual class EmbeddingStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<ImageEmbeddings>
) : StreamAnalyzer {
    private val client by lazy {
        baseOptions()
            .setResultListener(::onResult)
            .setErrorListener(callback::onAnalyzerError)
            .setRunningMode(LIVE_STREAM).client(context)
    }

    actual override fun analyze(image: CameraImage) {
        val input = BitmapImageBuilder(image.bitmap).build()
        client.embedAsync(input, image.timestamp)
    }

    private fun onResult(result: ImageEmbedderResult, input: MPImage) {
        result.analyze()
            .onSuccess(callback::onAnalyzerResult)
            .onFailure(callback::onAnalyzerError)
    }
}

private fun baseOptions() = ImageEmbedderOptions.builder()
    .setBaseOptions(
        BaseOptions.builder()
            .setModelAssetPath(MODEL_ASSET)
            .build()
    )
    .setQuantize(true)

private fun ImageEmbedderOptions.Builder.client(context: Context) = let {
    ImageEmbedder.createFromOptions(context, it.build())
}

private fun ImageEmbedderResult.analyze(): Result<ImageEmbeddings> {
    val embeddings = embeddingResult().embeddings().map(::map)
    return Result.success(embeddings)
}


private fun map(data: Embedding) = data.quantizedEmbedding().toByteString()
