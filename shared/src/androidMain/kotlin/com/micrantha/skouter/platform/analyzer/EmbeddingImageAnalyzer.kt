package com.micrantha.skouter.platform.analyzer

import android.content.Context
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.components.containers.Embedding
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder.ImageEmbedderOptions
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageEmbedding
import com.micrantha.skouter.platform.ImageEmbeddings

actual class EmbeddingImageAnalyzer(context: Context) : ImageAnalyzer<ImageEmbeddings> {

    private val client by lazy {
        val options = ImageEmbedderOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .setModelAssetPath("embeddings.tflite")
                    .build()
            )
            .setQuantize(true)
            .setRunningMode(IMAGE)
            .build()
        ImageEmbedder.createFromOptions(context, options)
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageEmbeddings> = try {
        val input = BitmapImageBuilder(image.bitmap).build()

        val result = client.embed(input).embeddingResult().embeddings().map(::map)

        Result.success(result)
    } catch (err: Throwable) {
        Result.failure(err)
    }

    private fun map(data: Embedding) = ImageEmbedding(
        data.quantizedEmbedding()
    )
}
