package com.micrantha.skouter

import android.content.Context
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.platform.ClueAnalyzer
import com.micrantha.skouter.platform.analyzer.ColorImageAnalyzer
import com.micrantha.skouter.platform.analyzer.LabelImageAnalyzer
import com.micrantha.skouter.platform.analyzer.ObjectImageAnalyzer
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindInstance
import org.kodein.di.bindProvider
import org.kodein.di.bindSingletonOf
import org.kodein.di.instance

fun androidDependencies(
    context: Context,
) = DI {
    bindInstance { context }

    bindSingletonOf(::Platform)

    bindProvider { LabelImageAnalyzer() }
    bindProvider { ColorImageAnalyzer(context) }
    bindProvider { ObjectImageAnalyzer() }

    bindFactory<Dispatch, ClueAnalyzer> { dispatch -> ClueAnalyzer(dispatch, instance()) }
}
