package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanLabels


expect class LabelCaptureAnalyzer : CaptureAnalyzer<ScanLabels>, StreamAnalyzer<ScanLabels> {
    override suspend fun analyzeCapture(image: CameraImage): Result<ScanLabels>

    override fun analyzeStream(image: CameraImage, callback: AnalyzerCallback<ScanLabels>)
}
