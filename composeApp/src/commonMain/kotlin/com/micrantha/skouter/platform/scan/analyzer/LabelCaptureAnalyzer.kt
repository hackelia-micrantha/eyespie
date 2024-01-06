package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer

expect class LabelCaptureAnalyzer : CaptureAnalyzer<LabelProof> {
    override suspend fun analyze(image: CameraImage): Result<LabelProof>
}