package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageObjects

expect class ObjectCaptureAnalyzer : CaptureAnalyzer<ImageObjects> {
    override suspend fun analyze(image: CameraImage): Result<ImageObjects>
}

expect class ObjectStreamAnalyzer : StreamAnalyzer {
    override fun analyze(image: CameraImage)
}
