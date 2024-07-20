package com.micrantha.eyespie.platform.scan.analyzer

import com.micrantha.eyespie.domain.model.MatchProof
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer

expect class MatchCaptureAnalyzer : CaptureAnalyzer<MatchProof> {
    override suspend fun analyze(image: CameraImage): Result<MatchProof>
}
