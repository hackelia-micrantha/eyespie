package com.micrantha.skouter.data.clue.source

import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.analyzer.SegmentCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.SegmentStreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageSegments
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

typealias SegmentCaptureLocalSource = SegmentCaptureAnalyzer


class SegmentStreamLocalSource(
    override val di: DI,
    callback: AnalyzerCallback<ImageSegments>
) : DIAware {
    private val analyzer by instance<AnalyzerCallback<ImageSegments>, SegmentStreamAnalyzer>(
        arg = callback
    )

    fun analyze(image: CameraImage) = analyzer.analyze(image)
}
