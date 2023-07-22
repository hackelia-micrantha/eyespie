package com.micrantha.skouter.data.clue.source

import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.analyzer.ColorCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.ColorStreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageColors
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

typealias ColorCaptureLocalSource = ColorCaptureAnalyzer

class ColorStreamLocalSource(
    override val di: DI,
    callback: AnalyzerCallback<ImageColors>
) : DIAware {

    private val analyzer by instance<AnalyzerCallback<ImageColors>, ColorStreamAnalyzer>(arg = callback)

    fun analyze(image: CameraImage) = analyzer.analyze(image)
}
