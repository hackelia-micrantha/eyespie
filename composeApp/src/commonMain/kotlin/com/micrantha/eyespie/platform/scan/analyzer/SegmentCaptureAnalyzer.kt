package com.micrantha.eyespie.platform.scan.analyzer

import com.micrantha.eyespie.domain.entities.SegmentProof
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer

expect class SegmentCaptureAnalyzer : CaptureAnalyzer<SegmentProof> {
    override suspend fun analyze(image: CameraImage): Result<SegmentProof>
}
