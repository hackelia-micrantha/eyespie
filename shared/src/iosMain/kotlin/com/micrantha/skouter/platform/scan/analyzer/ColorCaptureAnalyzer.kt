package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageColors

actual class ColorCaptureAnalyzer : CaptureAnalyzer<ImageColors> {
    actual override suspend fun analyze(image: CameraImage): Result<ImageColors> {
        return Result.failure(NotImplementedError())
    }
}

actual class ColorStreamAnalyzer : StreamAnalyzer {
    actual override fun analyze(image: CameraImage) = Unit
}
