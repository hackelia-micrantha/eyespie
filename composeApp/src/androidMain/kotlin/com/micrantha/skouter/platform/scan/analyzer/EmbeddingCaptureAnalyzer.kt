package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanEmbedding
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.processor.SearcherOptions
import org.tensorflow.lite.task.vision.searcher.ImageSearcher
import org.tensorflow.lite.task.vision.searcher.ImageSearcher.ImageSearcherOptions

private const val MODEL_ASSET = "models/embeddings/image.tflite"

actual class EmbeddingCaptureAnalyzer(
    context: Context,
) : AndroidCaptureAnalyzer<ScanEmbedding>(), CaptureAnalyzer<ScanEmbedding>,
    StreamAnalyzer<ScanEmbedding> {

    private val client by lazy {
        val options = ImageSearcherOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .useGpu()
                    .build()
            )
            .setSearcherOptions(
                SearcherOptions.builder()
                    .setQuantize(true)
                    .build()
            )
            .build()
        ImageSearcher.createFromFileAndOptions(context, MODEL_ASSET, options)
    }

    actual override suspend fun analyzeCapture(image: CameraImage): Result<ScanEmbedding> = try {
        val result = client.search(
            TensorImage.fromBitmap(image.bitmap),
            image.processingOptions()
        )
        Result.success(result.map { it.distance }.toFloatArray())
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
