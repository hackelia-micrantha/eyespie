package com.micrantha.bluebell.data

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

typealias Log = Napier

fun Log.init() {
    base(DebugAntilog())
}
