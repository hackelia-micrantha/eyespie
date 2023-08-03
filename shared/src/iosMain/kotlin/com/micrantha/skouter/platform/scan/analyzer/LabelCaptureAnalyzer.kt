package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraAnalyzer
import com.micrantha.skouter.platform.scan.CameraCaptureAnalyzer
import com.micrantha.skouter.platform.scan.CameraStreamAnalyzer
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageLabel
import com.micrantha.skouter.platform.scan.model.ImageLabels
import platform.Vision.VNClassificationObservation
import platform.Vision.VNClassifyImageRequest

typealias LabelAnalyzerConfig = CameraAnalyzer<ImageLabels, VNClassifyImageRequest, VNClassificationObservation>

private fun config(): LabelAnalyzerConfig = object : LabelAnalyzerConfig {

    override val filter = { results: List<*>? ->
        results?.filterIsInstance<VNClassificationObservation>() ?: emptyList()
    }

    override fun request() = VNClassifyImageRequest()

    override fun map(response: List<VNClassificationObservation>): ImageLabels = response.map {
        ImageLabel(it.identifier, it.confidence)
    }
}

actual class LabelCaptureAnalyzer(
    config: LabelAnalyzerConfig = config()
) : CameraCaptureAnalyzer<ImageLabels, VNClassifyImageRequest, VNClassificationObservation>(config),
    CaptureAnalyzer<ImageLabels>


actual class LabelStreamAnalyzer(
    callback: AnalyzerCallback<ImageLabels>,
    config: LabelAnalyzerConfig = config(),
) : CameraStreamAnalyzer<ImageLabels, VNClassifyImageRequest, VNClassificationObservation>(
    config, callback
), StreamAnalyzer
