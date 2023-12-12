package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraCaptureAnalyzer
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanLabels
import platform.Vision.VNClassificationObservation
import platform.Vision.VNClassifyImageRequest

typealias LabelAnalyzerConfig = CameraAnalyzerConfig<ScanLabels, VNClassifyImageRequest, VNClassificationObservation>

actual class LabelCaptureAnalyzer(
    config: LabelAnalyzerConfig = config()
) : CameraCaptureAnalyzer<ScanLabels, VNClassifyImageRequest, VNClassificationObservation>(config),
    CaptureAnalyzer<ScanLabels>, StreamAnalyzer<ScanLabels> {

    actual override suspend fun analyzeCapture(image: CameraImage): Result<ScanLabels> =
        super.analyzeCapture(image)

    actual override fun analyzeStream(image: CameraImage, callback: AnalyzerCallback<ScanLabels>) =
        super.analyzeStream(image, callback)
}

private fun config(): LabelAnalyzerConfig = object : LabelAnalyzerConfig {

    override val filter = { results: List<*>? ->
        results?.filterIsInstance<VNClassificationObservation>() ?: emptyList()
    }

    override fun request() = VNClassifyImageRequest()

    override fun map(response: List<VNClassificationObservation>, image: CameraImage): ScanLabels =
        response
}
