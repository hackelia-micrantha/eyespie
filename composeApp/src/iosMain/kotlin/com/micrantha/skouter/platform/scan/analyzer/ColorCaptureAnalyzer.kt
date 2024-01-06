package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer

actual class ColorCaptureAnalyzer : CaptureAnalyzer<ColorProof> {
    actual override suspend fun analyze(image: CameraImage): Result<ColorProof> {
        return Result.failure(NotImplementedError())
    }
}
