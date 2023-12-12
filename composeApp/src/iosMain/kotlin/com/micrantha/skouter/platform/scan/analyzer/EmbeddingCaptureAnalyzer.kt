package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraCaptureAnalyzer
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanEmbedding
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import platform.Vision.VNFeaturePrintObservation
import platform.Vision.VNGenerateImageFeaturePrintRequest

typealias EmbeddingAnalyzerConfig = CameraAnalyzerConfig<ScanEmbedding, VNGenerateImageFeaturePrintRequest, VNFeaturePrintObservation>

actual class EmbeddingCaptureAnalyzer(
    config: EmbeddingAnalyzerConfig = config()
) : CameraCaptureAnalyzer<ScanEmbedding, VNGenerateImageFeaturePrintRequest, VNFeaturePrintObservation>(
    config
), CaptureAnalyzer<ScanEmbedding>, StreamAnalyzer<ScanEmbedding>


@OptIn(ExperimentalForeignApi::class)
private fun config(): EmbeddingAnalyzerConfig = object : EmbeddingAnalyzerConfig {

    override val filter = { results: List<*>? ->
        results?.filterIsInstance<VNFeaturePrintObservation>() ?: emptyList()
    }

    override fun request() = VNGenerateImageFeaturePrintRequest()

    override fun map(
        response: List<VNFeaturePrintObservation>,
        image: CameraImage
    ): ScanEmbedding {
        val observation = response.minBy { it.confidence }
        return observation.data.bytes()!!.readBytes(observation.data.length.toInt()).map {
            it.toFloat()
        }.toFloatArray()
    }
}
