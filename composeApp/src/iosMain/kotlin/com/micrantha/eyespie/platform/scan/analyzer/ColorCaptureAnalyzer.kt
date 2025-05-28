package com.micrantha.eyespie.platform.scan.analyzer

import com.micrantha.eyespie.domain.entities.ColorProof
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer

actual class ColorCaptureAnalyzer : CaptureAnalyzer<ColorProof> {
    actual override suspend fun analyze(image: CameraImage): Result<ColorProof> {
        return Result.failure(NotImplementedError())
    }
}
