package com.micrantha.skouter

import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.platform.analyzer.ColorImageAnalyzer
import com.micrantha.skouter.platform.analyzer.EmbeddingImageAnalyzer
import com.micrantha.skouter.platform.analyzer.LabelImageAnalyzer
import com.micrantha.skouter.platform.analyzer.ObjectImageAnalyzer
import com.micrantha.skouter.platform.analyzer.SegmentImageAnalyzer
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingletonOf

fun iosModules() = DI {
    bindSingletonOf(::Platform)

    bindProvider { ObjectImageAnalyzer() }
    bindProvider { LabelImageAnalyzer() }
    bindProvider { ColorImageAnalyzer() }
    bindProvider { EmbeddingImageAnalyzer() }
    bindProvider { SegmentImageAnalyzer() }
}
