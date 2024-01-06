package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.domain.model.SegmentProof
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer

expect class SegmentCaptureAnalyzer : CaptureAnalyzer<SegmentProof> {
    override suspend fun analyze(image: CameraImage): Result<SegmentProof>
}
