package com.micrantha.skouter

import android.content.Context
import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.platform.CameraAnalyzer
import com.micrantha.skouter.platform.CameraAnalyzerOptions
import com.micrantha.skouter.platform.analyzer.ColorImageAnalyzer
import com.micrantha.skouter.platform.analyzer.LabelImageAnalyzer
import com.micrantha.skouter.platform.analyzer.ObjectImageAnalyzer
import com.micrantha.skouter.platform.analyzer.SegmentImageAnalyzer
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindInstance
import org.kodein.di.bindProvider
import org.kodein.di.bindSingletonOf

fun androidDependencies(
    context: Context,
) = DI {
    bindInstance { context }

    bindSingletonOf(::Platform)

    bindProvider { LabelImageAnalyzer() }
    bindProvider { ColorImageAnalyzer(context) }
    bindProvider { ObjectImageAnalyzer() }
    bindProvider { SegmentImageAnalyzer(context) }

    bindFactory<CameraAnalyzerOptions, CameraAnalyzer> { options ->
        CameraAnalyzer(options)
    }
}
