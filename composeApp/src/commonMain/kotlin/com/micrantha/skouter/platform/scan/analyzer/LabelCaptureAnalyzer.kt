package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageLabels

expect class LabelCaptureAnalyzer : CaptureAnalyzer<ImageLabels> {
    override suspend fun analyze(image: CameraImage): Result<ImageLabels>
}

expect class LabelStreamAnalyzer : StreamAnalyzer {
    override fun analyze(image: CameraImage)
}