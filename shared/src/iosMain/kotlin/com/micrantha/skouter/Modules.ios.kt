package com.micrantha.skouter

import com.micrantha.bluebell.Platform
import org.kodein.di.DI
import org.kodein.di.bindSingletonOf

fun iosModules() = DI.Module {
    bindSingletonOf(::Platform)
}
