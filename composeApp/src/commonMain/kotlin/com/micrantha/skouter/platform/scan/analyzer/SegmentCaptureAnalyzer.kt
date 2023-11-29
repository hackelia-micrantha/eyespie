package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageSegments

expect class SegmentCaptureAnalyzer : CaptureAnalyzer<ImageSegments> {
    override suspend fun analyze(image: CameraImage): Result<ImageSegments>
}

expect class SegmentStreamAnalyzer : StreamAnalyzer {

    override fun analyze(image: CameraImage)
}
