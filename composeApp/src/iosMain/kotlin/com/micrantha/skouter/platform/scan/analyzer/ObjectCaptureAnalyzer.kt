package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraCaptureAnalyzer
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanObjects
import platform.Vision.VNCoreMLRequest
import platform.Vision.VNRecognizedObjectObservation

typealias ObjectCaptureConfig = CameraAnalyzerConfig<ScanObjects, VNCoreMLRequest, VNRecognizedObjectObservation>

actual class ObjectCaptureAnalyzer(config: ObjectCaptureConfig = config()) :
    CameraCaptureAnalyzer<ScanObjects, VNCoreMLRequest, VNRecognizedObjectObservation>(config),
    CaptureAnalyzer<ScanObjects>, StreamAnalyzer<ScanObjects>


private fun config(): ObjectCaptureConfig = object : ObjectCaptureConfig {
    override fun request() = VNCoreMLRequest()

    override val filter = { results: List<*>? ->
        results?.filterIsInstance<VNRecognizedObjectObservation>() ?: emptyList()
    }

    override fun map(
        response: List<VNRecognizedObjectObservation>,
        image: CameraImage
    ): ScanObjects = response

}
