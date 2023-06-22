package com.micrantha.skouter.platform.analyzer

import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageEmbeddings

actual class EmbeddingImageAnalyzer : ImageAnalyzer<ImageEmbeddings> {

    actual override suspend fun analyze(image: CameraImage): Result<ImageEmbeddings> {
        // Use CoreML similarity search
        return Result.failure(NotImplementedError())
    }
}
