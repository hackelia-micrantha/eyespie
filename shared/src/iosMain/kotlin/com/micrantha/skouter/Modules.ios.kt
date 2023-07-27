package com.micrantha.skouter

import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.analyzer.ColorCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.ColorStreamAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.EmbeddingCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.EmbeddingStreamAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.LabelCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.LabelStreamAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.ObjectCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.ObjectStreamAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.SegmentCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.SegmentStreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageColors
import com.micrantha.skouter.platform.scan.model.ImageEmbeddings
import com.micrantha.skouter.platform.scan.model.ImageLabels
import com.micrantha.skouter.platform.scan.model.ImageObjects
import com.micrantha.skouter.platform.scan.model.ImageSegments
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingletonOf

fun iosModules() = DI {
    bindSingletonOf(::Platform)

    bindFactory { callback: AnalyzerCallback<ImageLabels> ->
        LabelStreamAnalyzer(callback)
    }
    bindProviderOf(::LabelCaptureAnalyzer)

    bindProviderOf(::ColorCaptureAnalyzer)
    bindFactory { callback: AnalyzerCallback<ImageColors> ->
        ColorStreamAnalyzer(callback)
    }
    bindProviderOf(::ObjectCaptureAnalyzer)
    bindFactory { callback: AnalyzerCallback<ImageObjects> ->
        ObjectStreamAnalyzer(callback)
    }
    bindProviderOf(::SegmentCaptureAnalyzer)
    bindFactory { callback: AnalyzerCallback<ImageSegments> ->
        SegmentStreamAnalyzer(callback)
    }
    bindProviderOf(::EmbeddingCaptureAnalyzer)
    bindFactory { callback: AnalyzerCallback<ImageEmbeddings> ->
        EmbeddingStreamAnalyzer(callback)
    }
}
