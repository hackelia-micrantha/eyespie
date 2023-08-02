package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraAnalyzer
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageLabel
import com.micrantha.skouter.platform.scan.model.ImageLabels
import platform.Vision.VNClassificationObservation
import platform.Vision.VNClassifyImageRequest

actual class LabelCaptureAnalyzer : CameraAnalyzer<ImageLabels, VNClassifyImageRequest>(),
    CaptureAnalyzer<ImageLabels> {

    override suspend fun map(response: List<*>): ImageLabels {
        val classifications = response.filterIsInstance<VNClassificationObservation>()
        return classifications.map {
            ImageLabel(it.identifier, it.confidence)
        }
    }

    override fun request() = VNClassifyImageRequest()
}

actual class LabelStreamAnalyzer(
    callback: AnalyzerCallback<ImageLabels>
) : StreamAnalyzer {
    actual override fun analyze(image: CameraImage) = Unit
}
