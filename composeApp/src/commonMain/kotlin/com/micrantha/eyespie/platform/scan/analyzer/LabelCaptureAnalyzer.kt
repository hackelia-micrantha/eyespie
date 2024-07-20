package com.micrantha.eyespie.platform.scan.analyzer

import com.micrantha.eyespie.domain.model.LabelProof
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer

expect class LabelCaptureAnalyzer : CaptureAnalyzer<LabelProof> {
    override suspend fun analyze(image: CameraImage): Result<LabelProof>
}