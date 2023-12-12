package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanColors

actual class ColorCaptureAnalyzer : CaptureAnalyzer<ScanColors>, StreamAnalyzer<ScanColors> {
    actual override suspend fun analyzeCapture(image: CameraImage): Result<ScanColors> {
        return Result.failure(NotImplementedError())
    }

    actual override fun analyzeStream(image: CameraImage, callback: AnalyzerCallback<ScanColors>) =
        Unit
}
