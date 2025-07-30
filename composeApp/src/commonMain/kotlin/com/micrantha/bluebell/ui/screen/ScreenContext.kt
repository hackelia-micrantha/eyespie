package com.micrantha.bluebell.ui.screen

import androidx.compose.runtime.compositionLocalOf
import cafe.adriel.voyager.core.screen.Screen
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.domain.repository.LocalizedRepository
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.bluebell.platform.Platform
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.components.Router.Options
import com.micrantha.bluebell.ui.components.Router.Options.None
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.factory
import org.kodein.di.provider

interface ScreenContext : DIAware {
    val i18n: LocalizedRepository

    val router: Router

    val dispatcher: Dispatcher

    val fileSystem: FileSystem
}

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

inline fun <reified T : Screen> ScreenContext.navigate(options: Options = None) {
    val screen: () -> T by di.provider()
    router.navigate(screen(), options)
}

inline fun <reified T : Screen, reified A : Any> ScreenContext.navigate(
    options: Options = None,
    arg: A
) {
    val screen: (arg: A) -> T by di.factory<A, T>()
    router.navigate(screen(arg), options)
}
