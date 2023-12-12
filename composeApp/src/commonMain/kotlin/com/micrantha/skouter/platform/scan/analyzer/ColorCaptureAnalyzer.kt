package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanColors

expect class ColorCaptureAnalyzer : CaptureAnalyzer<ScanColors>, StreamAnalyzer<ScanColors> {
    override suspend fun analyzeCapture(image: CameraImage): Result<ScanColors>
    override fun analyzeStream(image: CameraImage, callback: AnalyzerCallback<ScanColors>)
}
