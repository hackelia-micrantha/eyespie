package com.micrantha.skouter

import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.platform.ImageAnalyzer
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingletonOf

fun iosModules() = DI {
    bindSingletonOf(::Platform)

    bindProvider { ImageAnalyzer() }
}
