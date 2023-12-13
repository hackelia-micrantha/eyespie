package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageColors

actual class ColorCaptureAnalyzer : CaptureAnalyzer<ImageColors> {
    actual override suspend fun analyze(image: CameraImage): Result<ImageColors> {
        return Result.failure(NotImplementedError())
    }
}
