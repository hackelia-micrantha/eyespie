package com.micrantha.skouter

import android.content.Context
import com.micrantha.bluebell.Platform
import org.kodein.di.DI
import org.kodein.di.bindInstance
import org.kodein.di.bindSingleton

fun androidDependencies(context: Context) = DI {
    bindInstance { context }

    bindSingleton { Platform(context) }
}
