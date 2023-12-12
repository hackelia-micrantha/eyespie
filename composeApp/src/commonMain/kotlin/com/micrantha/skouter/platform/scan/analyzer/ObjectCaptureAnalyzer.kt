package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanObjects

expect class ObjectCaptureAnalyzer : CaptureAnalyzer<ScanObjects>, StreamAnalyzer<ScanObjects> {
    override suspend fun analyzeCapture(image: CameraImage): Result<ScanObjects>
    override fun analyzeStream(image: CameraImage, callback: AnalyzerCallback<ScanObjects>)
}
