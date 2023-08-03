package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageEmbeddings

actual class EmbeddingCaptureAnalyzer : CaptureAnalyzer<ImageEmbeddings> {

    actual override suspend fun analyze(image: CameraImage): Result<ImageEmbeddings> {
        // Use CoreML similarity search
        return Result.failure(NotImplementedError())
    }
}

actual class EmbeddingStreamAnalyzer(
    callback: AnalyzerCallback<ImageEmbeddings>
) : StreamAnalyzer {
    actual override fun analyze(image: CameraImage) = Unit
}
