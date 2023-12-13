package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraCaptureAnalyzer
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CameraStreamAnalyzer
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageLabel
import com.micrantha.skouter.platform.scan.model.ImageLabels
import platform.Vision.VNClassificationObservation
import platform.Vision.VNClassifyImageRequest

typealias LabelAnalyzerConfig = CameraAnalyzerConfig<ImageLabels, VNClassifyImageRequest, VNClassificationObservation>

actual class LabelCaptureAnalyzer(
    config: LabelAnalyzerConfig = config()
) : CameraCaptureAnalyzer<ImageLabels, VNClassifyImageRequest, VNClassificationObservation>(config),
    CaptureAnalyzer<ImageLabels>


class LabelStreamAnalyzer(
    callback: AnalyzerCallback<ImageLabels>,
    config: LabelAnalyzerConfig = config(),
) : CameraStreamAnalyzer<ImageLabels, VNClassifyImageRequest, VNClassificationObservation>(
    config, callback
), StreamAnalyzer

private fun config(): LabelAnalyzerConfig = object : LabelAnalyzerConfig {

    override val filter = { results: List<*>? ->
        results?.filterIsInstance<VNClassificationObservation>() ?: emptyList()
    }

    override fun request() = VNClassifyImageRequest()

    override fun map(response: List<VNClassificationObservation>, image: CameraImage): ImageLabels =
        response.map {
            ImageLabel(it.identifier, it.confidence)
        }
}
