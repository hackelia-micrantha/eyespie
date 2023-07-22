package com.micrantha.skouter.data.clue.source

import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.analyzer.ObjectCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.ObjectStreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageObjects
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

typealias ObjectCaptureLocalSource = ObjectCaptureAnalyzer

class ObjectStreamLocalSource(
    override val di: DI,
    callback: AnalyzerCallback<ImageObjects>
) : DIAware {
    private val analyzer by instance<AnalyzerCallback<ImageObjects>, ObjectStreamAnalyzer>(
        arg = callback
    )

    fun analyze(image: CameraImage) = analyzer.analyze(image)
}
