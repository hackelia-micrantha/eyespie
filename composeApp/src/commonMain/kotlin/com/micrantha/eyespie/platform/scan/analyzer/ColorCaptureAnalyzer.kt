package com.micrantha.eyespie.platform.scan.analyzer

import com.micrantha.eyespie.domain.model.ColorProof
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer

expect class ColorCaptureAnalyzer : CaptureAnalyzer<ColorProof> {
    override suspend fun analyze(image: CameraImage): Result<ColorProof>
}
