package com.micrantha.skouter.data.clue.source

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.analyzer.EmbeddingCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.EmbeddingStreamAnalyzer
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.model.ImageEmbeddings
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.kodein.di.on

typealias MatchCaptureLocalSource = EmbeddingCaptureAnalyzer

class MatchStreamLocalSource(
    override val di: DI,
    callback: AnalyzerCallback<ImageEmbeddings>
) : DIAware {
    private val analyzer by di.on(this)
        .instance<AnalyzerCallback<ImageEmbeddings>, EmbeddingStreamAnalyzer>(
            arg = callback
        )

    fun analyze(image: CameraImage) = analyzer.analyze(image)
}
