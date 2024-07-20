package com.micrantha.eyespie.platform.scan.analyzer

import com.micrantha.eyespie.domain.model.LabelClue
import com.micrantha.eyespie.domain.model.LabelProof
import com.micrantha.eyespie.platform.scan.CameraAnalyzerConfig
import com.micrantha.eyespie.platform.scan.CameraCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.CameraStreamAnalyzer
import com.micrantha.eyespie.platform.scan.components.AnalyzerCallback
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer
import com.micrantha.eyespie.platform.scan.components.StreamAnalyzer
import platform.Vision.VNClassificationObservation
import platform.Vision.VNClassifyImageRequest

typealias LabelAnalyzerConfig = CameraAnalyzerConfig<LabelProof, VNClassifyImageRequest, VNClassificationObservation>

actual class LabelCaptureAnalyzer :
    CameraCaptureAnalyzer<LabelProof, VNClassifyImageRequest, VNClassificationObservation>(config()),
    CaptureAnalyzer<LabelProof>


class LabelStreamAnalyzer(
    callback: AnalyzerCallback<LabelProof>,
) : CameraStreamAnalyzer<LabelProof, VNClassifyImageRequest, VNClassificationObservation>(
    config(), callback
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
