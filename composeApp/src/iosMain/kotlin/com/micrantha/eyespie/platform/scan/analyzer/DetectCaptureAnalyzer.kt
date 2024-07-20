package com.micrantha.eyespie.platform.scan.analyzer

import androidx.compose.ui.geometry.Rect
import com.micrantha.eyespie.domain.model.DetectClue
import com.micrantha.eyespie.domain.model.DetectProof
import com.micrantha.eyespie.domain.model.LabelClue
import com.micrantha.eyespie.platform.scan.CameraAnalyzerConfig
import com.micrantha.eyespie.platform.scan.CameraCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.CameraStreamAnalyzer
import com.micrantha.eyespie.platform.scan.components.AnalyzerCallback
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer
import com.micrantha.eyespie.platform.scan.components.StreamAnalyzer
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectGetHeight
import platform.CoreGraphics.CGRectGetMinX
import platform.CoreGraphics.CGRectGetMinY
import platform.CoreGraphics.CGRectGetWidth
import platform.Vision.VNClassificationObservation
import platform.Vision.VNCoreMLRequest
import platform.Vision.VNRecognizedObjectObservation

typealias ObjectCaptureConfig = CameraAnalyzerConfig<DetectProof, VNCoreMLRequest, VNRecognizedObjectObservation>

actual class DetectCaptureAnalyzer :
    CameraCaptureAnalyzer<DetectProof, VNCoreMLRequest, VNRecognizedObjectObservation>(config()),
    CaptureAnalyzer<DetectProof>

class DetectStreamAnalyzer(
    callback: AnalyzerCallback<DetectProof>,
) : CameraStreamAnalyzer<DetectProof, VNCoreMLRequest, VNRecognizedObjectObservation>(
    config(),
    callback
), StreamAnalyzer

@OptIn(ExperimentalForeignApi::class)
private fun config(): ObjectCaptureConfig = object : ObjectCaptureConfig {
    override fun request() = VNCoreMLRequest()

    override val filter = { results: List<*>? ->
        results?.filterIsInstance<VNRecognizedObjectObservation>() ?: emptyList()
    }

    override fun map(
        response: List<VNRecognizedObjectObservation>,
        image: CameraImage
    ): DetectProof {
        return response.map { obj ->
            DetectClue(
                data = Rect(
                    CGRectGetMinX(obj.boundingBox).toFloat(),
                    CGRectGetMinY(obj.boundingBox).toFloat(),
                    CGRectGetWidth(obj.boundingBox).toFloat(),
                    CGRectGetHeight(obj.boundingBox).toFloat()
                ),
                labels = obj.labels.filterIsInstance<VNClassificationObservation>().map {
                    LabelClue(it.identifier, it.confidence)
                }.toSet()
            )
        }.toSet()
    }

}
