package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraCaptureAnalyzer
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CameraStreamAnalyzer
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageEmbeddings
import okio.ByteString.Companion.toByteString
import platform.Vision.VNFeaturePrintObservation
import platform.Vision.VNGenerateImageFeaturePrintRequest

typealias EmbeddingAnalyzerConfig = CameraAnalyzerConfig<ImageEmbeddings, VNGenerateImageFeaturePrintRequest, VNFeaturePrintObservation>

actual class MatchCaptureAnalyzer(
    config: EmbeddingAnalyzerConfig = config()
) : CameraCaptureAnalyzer<ImageEmbeddings, VNGenerateImageFeaturePrintRequest, VNFeaturePrintObservation>(
    config
), CaptureAnalyzer<ImageEmbeddings>

class EmbeddingStreamAnalyzer(
    callback: AnalyzerCallback<ImageEmbeddings>,
    config: EmbeddingAnalyzerConfig = config(),
) : CameraStreamAnalyzer<ImageEmbeddings, VNGenerateImageFeaturePrintRequest, VNFeaturePrintObservation>(
    config,
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
    ): ImageEmbeddings = response.map {
        it.data.toByteString()
    }
}
