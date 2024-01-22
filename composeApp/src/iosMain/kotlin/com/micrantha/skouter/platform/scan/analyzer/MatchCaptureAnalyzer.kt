package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.domain.model.MatchClue
import com.micrantha.skouter.domain.model.MatchProof
import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraCaptureAnalyzer
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CameraStreamAnalyzer
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
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
