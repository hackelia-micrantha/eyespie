package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageSegments

actual class SegmentCaptureAnalyzer : CaptureAnalyzer<ImageSegments> {
    actual override suspend fun analyze(image: CameraImage): Result<ImageSegments> {
        return Result.failure(NotImplementedError())
    }
}

actual class SegmentStreamAnalyzer : StreamAnalyzer {
    actual override fun analyze(image: CameraImage) = Unit
}
