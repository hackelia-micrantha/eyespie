package com.micrantha.skouter.platform.scan.analyzer

import androidx.compose.ui.geometry.Rect
import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraCaptureAnalyzer
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CameraStreamAnalyzer
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageLabel
import com.micrantha.skouter.platform.scan.model.ImageObject
import com.micrantha.skouter.platform.scan.model.ImageObjects
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectGetHeight
import platform.CoreGraphics.CGRectGetMinX
import platform.CoreGraphics.CGRectGetMinY
import platform.CoreGraphics.CGRectGetWidth
import platform.Vision.VNClassificationObservation
import platform.Vision.VNCoreMLRequest
import platform.Vision.VNRecognizedObjectObservation

typealias ObjectCaptureConfig = CameraAnalyzerConfig<ImageObjects, VNCoreMLRequest, VNRecognizedObjectObservation>

actual class ObjectCaptureAnalyzer(config: ObjectCaptureConfig = config()) :
    CameraCaptureAnalyzer<ImageObjects, VNCoreMLRequest, VNRecognizedObjectObservation>(config),
    CaptureAnalyzer<ImageObjects>

actual class ObjectStreamAnalyzer(
    callback: AnalyzerCallback<ImageObjects>,
    config: ObjectCaptureConfig = config(),
) : CameraStreamAnalyzer<ImageObjects, VNCoreMLRequest, VNRecognizedObjectObservation>(
    config,
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
    ): ImageObjects {
        return response.map { obj ->
            ImageObject(
                Rect(
                    CGRectGetMinX(obj.boundingBox).toFloat(),
                    CGRectGetMinY(obj.boundingBox).toFloat(),
                    CGRectGetWidth(obj.boundingBox).toFloat(),
                    CGRectGetHeight(obj.boundingBox).toFloat()
                ),
                obj.labels.filterIsInstance<VNClassificationObservation>().map {
                    ImageLabel(it.identifier, it.confidence)
                }
            )
        }
    }

}
