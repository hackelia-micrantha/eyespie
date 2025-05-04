package com.micrantha.eyespie.platform.scan.analyzer

import com.micrantha.eyespie.domain.entities.MatchClue
import com.micrantha.eyespie.domain.entities.MatchProof
import com.micrantha.eyespie.platform.scan.CameraAnalyzerConfig
import com.micrantha.eyespie.platform.scan.CameraCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.CameraStreamAnalyzer
import com.micrantha.eyespie.platform.scan.components.AnalyzerCallback
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer
import com.micrantha.eyespie.platform.scan.components.StreamAnalyzer
import okio.ByteString.Companion.toByteString
import platform.Vision.VNFeaturePrintObservation
import platform.Vision.VNGenerateImageFeaturePrintRequest

typealias EmbeddingAnalyzerConfig = CameraAnalyzerConfig<MatchProof, VNGenerateImageFeaturePrintRequest, VNFeaturePrintObservation>

actual class MatchCaptureAnalyzer :
    CameraCaptureAnalyzer<MatchProof, VNGenerateImageFeaturePrintRequest, VNFeaturePrintObservation>(
        config()
    ), CaptureAnalyzer<MatchProof>

class EmbeddingStreamAnalyzer(
    callback: AnalyzerCallback<MatchProof>
) : CameraStreamAnalyzer<MatchProof, VNGenerateImageFeaturePrintRequest, VNFeaturePrintObservation>(
    config(),
    callback
), StreamAnalyzer


private fun config(): EmbeddingAnalyzerConfig = object : EmbeddingAnalyzerConfig {

    override val filter = { results: List<*>? ->
        results?.filterIsInstance<VNFeaturePrintObservation>() ?: emptyList()
    }

    override fun request() = VNGenerateImageFeaturePrintRequest()

    override fun map(
        response: List<VNFeaturePrintObservation>,
        image: CameraImage
    ): MatchProof = response.map {
        MatchClue(it.data.toByteString())
    }
}
