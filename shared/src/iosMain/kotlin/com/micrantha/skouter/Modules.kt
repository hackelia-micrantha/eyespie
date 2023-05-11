package com.micrantha.skouter

import com.micrantha.bluebell.Platform
import org.koin.dsl.module

fun iosModules() = module {
    single { Platform() }
}
