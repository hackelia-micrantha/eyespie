package com.micrantha.bluebell.ui.screen

import androidx.compose.runtime.compositionLocalOf
import cafe.adriel.voyager.core.screen.Screen
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.bluebell.platform.Platform
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.ui.components.Router
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.instance

interface ScreenContext : DIAware {
    val i18n: LocalizedRepository

    val router: Router

    val dispatcher: Dispatcher

    val fileSystem: FileSystem
}

inline fun <reified T : Screen> ScreenContext.get() = direct.instance<T>()

inline fun <reified T : Screen, reified A : Any> ScreenContext.get(arg: A) =
    direct.instance<A, T>(arg = arg)


class BluebellScreenContext(
    override val di: DI,
    platform: Platform,
    override val dispatcher: Dispatcher,
    override val router: Router
) : ScreenContext {
    override val i18n: LocalizedRepository = platform

    override val fileSystem: FileSystem = platform
}

val LocalScreenContext = compositionLocalOf<ScreenContext> {
    error("Local screen context not defined")
}

val LocalDispatcher = compositionLocalOf<Dispatcher> {
    error("Local dispatcher not defined")
}
