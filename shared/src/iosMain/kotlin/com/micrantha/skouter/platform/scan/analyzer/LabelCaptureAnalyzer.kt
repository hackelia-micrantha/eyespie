package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageLabels

actual class LabelCaptureAnalyzer : CaptureAnalyzer<ImageLabels> {

    actual override suspend fun analyze(image: CameraImage): Result<ImageLabels> {
        return Result.failure(NotImplementedError())
    }
}

actual class LabelStreamAnalyzer : StreamAnalyzer {
    actual override fun analyze(image: CameraImage) = Unit
}
