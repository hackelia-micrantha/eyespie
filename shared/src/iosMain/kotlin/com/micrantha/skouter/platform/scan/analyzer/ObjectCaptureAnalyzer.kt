package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageObjects

actual class ObjectCaptureAnalyzer : CaptureAnalyzer<ImageObjects> {
    actual override suspend fun analyze(image: CameraImage): Result<ImageObjects> {
        return Result.failure(NotImplementedError())
    }
}

actual class ObjectStreamAnalyzer(
    callback: AnalyzerCallback<ImageObjects>
) : StreamAnalyzer {
    actual override fun analyze(image: CameraImage) = Unit
}
