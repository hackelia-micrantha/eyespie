package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanSegments

expect class SegmentCaptureAnalyzer : CaptureAnalyzer<ScanSegments>,
    StreamAnalyzer<ScanSegments> {
    override suspend fun analyzeCapture(image: CameraImage): Result<ScanSegments>

    override fun analyzeStream(image: CameraImage, callback: AnalyzerCallback<ScanSegments>)
}
