package com.micrantha.skouter

import com.micrantha.bluebell.Platform
import org.koin.dsl.module

fun androidModules() = module {
    single { Platform(get()) }
}
