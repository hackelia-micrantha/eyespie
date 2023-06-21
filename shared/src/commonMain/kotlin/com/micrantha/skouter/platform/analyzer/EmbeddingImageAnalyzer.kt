package com.micrantha.skouter.platform.analyzer

import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageEmbeddings

expect class EmbeddingImageAnalyzer : ImageAnalyzer<ImageEmbeddings> {
    override suspend fun analyze(image: CameraImage): Result<ImageEmbeddings>
}
