package com.micrantha.bluebell.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.micrantha.bluebell.Platform
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.components.Router.Options.Replace
import com.micrantha.bluebell.ui.components.Router.Options.Reset
import com.micrantha.bluebell.ui.components.isBackOrIdle
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bindSingleton
import org.kodein.di.compose.localDI
import org.kodein.di.compose.subDI
import org.kodein.di.direct
import org.kodein.di.instance

interface ScreenContext : DIAware, Router {
    val i18n: LocalizedRepository

    val dispatcher: Dispatcher
}

inline fun <reified T : Screen> ScreenContext.get() = direct.instance<T>()

inline fun <reified T : Screen, reified A : Any> ScreenContext.get(arg: A) =
    direct.instance<A, T>(arg = arg)

class BluebellScreenContext(
    override val di: DI,
    platform: Platform,
    override val dispatcher: Dispatcher,
    private val navigator: Navigator
) : ScreenContext {
    override val i18n: LocalizedRepository = platform

    override fun navigateBack() = navigator.pop()

    override val canGoBack: Boolean = navigator.canPop

    override val isBackOrIdle: Boolean = navigator.lastEvent.isBackOrIdle

    override fun <T : Screen> navigate(screen: T, options: Router.Options) = when (options) {
        Replace -> navigator.replace(screen)
        Reset -> navigator.replaceAll(screen)
        else -> navigator.push(screen)
    }


}

@Composable
fun withViewContext(di: DI = localDI(), content: @Composable () -> Unit) = subDI(
    parentDI = di,
    diBuilder = {
        bindSingleton { BluebellScreenContext(di, instance(), instance(), instance()) }
    }
) {
    content()
}

val LocalScreenContext = compositionLocalOf<ScreenContext> {
    error("Screen context not defined")
}

