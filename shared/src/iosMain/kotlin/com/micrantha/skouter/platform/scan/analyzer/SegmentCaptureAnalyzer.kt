package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageSegments

actual class SegmentCaptureAnalyzer : CaptureAnalyzer<ImageSegments> {
    actual override suspend fun analyze(image: CameraImage): Result<ImageSegments> {
        return Result.failure(NotImplementedError())
    }
}

actual class SegmentStreamAnalyzer(
    callback: AnalyzerCallback<ImageSegments>
) : StreamAnalyzer {
    actual override fun analyze(image: CameraImage) = Unit
}
