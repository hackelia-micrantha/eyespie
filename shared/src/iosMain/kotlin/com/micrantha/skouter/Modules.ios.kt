package com.micrantha.skouter

import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.platform.scan.analyzer.ColorCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.EmbeddingCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.LabelCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.ObjectCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.SegmentCaptureAnalyzer
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingletonOf

fun iosModules() = DI {
    bindSingletonOf(::Platform)

    bindProvider { ObjectCaptureAnalyzer() }
    bindProvider { LabelCaptureAnalyzer() }
    bindProvider { ColorCaptureAnalyzer() }
    bindProvider { EmbeddingCaptureAnalyzer() }
    bindProvider { SegmentCaptureAnalyzer() }
}
