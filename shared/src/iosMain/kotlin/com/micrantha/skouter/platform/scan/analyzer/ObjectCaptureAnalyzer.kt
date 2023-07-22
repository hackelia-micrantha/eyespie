package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageObjects

actual class ObjectCaptureAnalyzer : CaptureAnalyzer<ImageObjects> {
    actual override suspend fun analyze(image: CameraImage): Result<ImageObjects> {
        return Result.failure(NotImplementedError())
    }
}

actual class ObjectStreamAnalyzer : StreamAnalyzer {
    actual override fun analyze(image: CameraImage) = Unit
}
