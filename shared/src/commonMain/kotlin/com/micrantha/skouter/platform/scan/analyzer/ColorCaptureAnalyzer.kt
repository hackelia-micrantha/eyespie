package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageColors

expect class ColorCaptureAnalyzer : CaptureAnalyzer<ImageColors> {
    override suspend fun analyze(image: CameraImage): Result<ImageColors>
}

expect class ColorStreamAnalyzer : StreamAnalyzer {
    override fun analyze(image: CameraImage)
}