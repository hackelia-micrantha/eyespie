package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageEmbeddings

expect class EmbeddingCaptureAnalyzer : CaptureAnalyzer<ImageEmbeddings> {
    override suspend fun analyze(image: CameraImage): Result<ImageEmbeddings>
}

expect class EmbeddingStreamAnalyzer : StreamAnalyzer {
    override fun analyze(image: CameraImage)
}
