package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.domain.model.MatchProof
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer

expect class MatchCaptureAnalyzer : CaptureAnalyzer<MatchProof> {
    override suspend fun analyze(image: CameraImage): Result<MatchProof>
}
