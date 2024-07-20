package com.micrantha.eyespie.platform.scan.analyzer

import com.micrantha.eyespie.domain.model.DetectProof
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer

expect class DetectCaptureAnalyzer : CaptureAnalyzer<DetectProof> {
    override suspend fun analyze(image: CameraImage): Result<DetectProof>
}
