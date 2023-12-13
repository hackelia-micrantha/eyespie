package com.micrantha.skouter.data.clue.source

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.analyzer.LabelCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.LabelStreamAnalyzer
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.model.ImageLabels
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

typealias LabelCaptureLocalSource = LabelCaptureAnalyzer

class LabelStreamLocalSource(
    override val di: DI,
    callback: AnalyzerCallback<ImageLabels>
) : DIAware {
    private val analyzer by instance<AnalyzerCallback<ImageLabels>, LabelStreamAnalyzer>(arg = callback)

    fun analyze(image: CameraImage) = analyzer.analyze(image)
}
