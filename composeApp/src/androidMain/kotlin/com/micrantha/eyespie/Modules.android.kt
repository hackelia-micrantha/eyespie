package com.micrantha.eyespie

import android.content.Context
import com.micrantha.bluebell.get
import com.micrantha.bluebell.platform.AndroidNetworkMonitor
import com.micrantha.bluebell.platform.Platform
import com.micrantha.eyespie.platform.scan.analyzer.ColorCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.DetectCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.LabelCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.MatchCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.SegmentCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.generator.ImageStyler
import org.kodein.di.DI
import org.kodein.di.bindInstance
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingletonOf

fun androidDependencies(
    context: Context,
) = DI {
    bindInstance { context }

    bindSingletonOf(::Platform)

    bindProviderOf(::AndroidNetworkMonitor)

    bindProvider {
        LabelCaptureAnalyzer(get())
    }

    bindProvider {
        ColorCaptureAnalyzer(get())
    }

    bindProviderOf(::ImageStyler)

    bindProviderOf(::DetectCaptureAnalyzer)

    bindProviderOf(::SegmentCaptureAnalyzer)

    bindProviderOf(::MatchCaptureAnalyzer)
}
