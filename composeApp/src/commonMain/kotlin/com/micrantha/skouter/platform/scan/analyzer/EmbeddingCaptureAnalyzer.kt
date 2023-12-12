package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanEmbedding

expect class EmbeddingCaptureAnalyzer : CaptureAnalyzer<ScanEmbedding>,
    StreamAnalyzer<ScanEmbedding> {
    override suspend fun analyzeCapture(image: CameraImage): Result<ScanEmbedding>
    override fun analyzeStream(image: CameraImage, callback: AnalyzerCallback<ScanEmbedding>)
}
