package com.micrantha.eyespie

import com.micrantha.bluebell.platform.Platform
import com.micrantha.eyespie.platform.scan.analyzer.ColorCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.DetectCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.LabelCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.MatchCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.SegmentCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.generator.ImageObfuscator
import com.micrantha.eyespie.platform.scan.generator.ImageStyler
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingleton

fun iosModules(app: AppDelegate) = DI {

    bindSingleton { Platform(app) }

    bindProvider { app.networkMonitor }

    bindProviderOf(::LabelCaptureAnalyzer)
    bindProviderOf(::ColorCaptureAnalyzer)
    bindProviderOf(::DetectCaptureAnalyzer)
    bindProviderOf(::SegmentCaptureAnalyzer)
    bindProviderOf(::MatchCaptureAnalyzer)

    bindProviderOf(::ImageObfuscator)
    bindProviderOf(::ImageStyler)
}
