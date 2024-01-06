package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraCaptureAnalyzer
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CameraStreamAnalyzer
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import platform.Vision.VNClassificationObservation
import platform.Vision.VNClassifyImageRequest

typealias LabelAnalyzerConfig = CameraAnalyzerConfig<LabelProof, VNClassifyImageRequest, VNClassificationObservation>

actual class LabelCaptureAnalyzer(
    config: LabelAnalyzerConfig = config()
) : CameraCaptureAnalyzer<LabelProof, VNClassifyImageRequest, VNClassificationObservation>(config),
    CaptureAnalyzer<LabelProof>


class LabelStreamAnalyzer(
    callback: AnalyzerCallback<LabelProof>,
    config: LabelAnalyzerConfig = config(),
) : CameraStreamAnalyzer<LabelProof, VNClassifyImageRequest, VNClassificationObservation>(
    config, callback
), StreamAnalyzer

private fun config(): LabelAnalyzerConfig = object : LabelAnalyzerConfig {

    override val filter = { results: List<*>? ->
        results?.filterIsInstance<VNClassificationObservation>() ?: emptyList()
    }

    override fun request() = VNClassifyImageRequest()

    override fun map(response: List<VNClassificationObservation>, image: CameraImage): LabelProof =
        response.map {
            LabelClue(it.identifier, it.confidence)
        }.toSet()
}
