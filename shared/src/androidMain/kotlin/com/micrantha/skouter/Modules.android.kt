package com.micrantha.skouter

import android.content.Context
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.platform.ClueAnalyzer
import com.micrantha.skouter.platform.ImageLabelAnalyzer
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

    bindProvider { ImageLabelAnalyzer() }

    bindFactory<Dispatch, ClueAnalyzer> { dispatch -> ClueAnalyzer(dispatch, instance()) }
}
