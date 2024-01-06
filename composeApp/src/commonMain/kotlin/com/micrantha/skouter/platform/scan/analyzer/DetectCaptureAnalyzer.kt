package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer

expect class DetectCaptureAnalyzer : CaptureAnalyzer<DetectProof> {
    override suspend fun analyze(image: CameraImage): Result<DetectProof>
}
