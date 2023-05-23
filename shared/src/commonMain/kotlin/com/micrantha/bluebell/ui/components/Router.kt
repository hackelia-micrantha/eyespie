package com.micrantha.bluebell.ui.components

import cafe.adriel.voyager.core.screen.Screen
import com.micrantha.bluebell.ui.components.Router.Options
import com.micrantha.bluebell.ui.components.Router.Options.None
import org.kodein.di.DIAware
import org.kodein.di.provider

interface Router : DIAware {
    fun navigateBack(): Boolean

    val canGoBack: Boolean

    fun <T : Screen> navigate(screen: T, options: Options = None)

    val screen: Screen

    enum class Options {
        None,
        Replace,
        Reset
    }
}

inline fun <reified T : Screen> Router.navigate(options: Options = None) {
    val screen: () -> T by di.provider()
    navigate(screen(), options)
}

inline fun <reified T : Screen, reified A : Any> Router.navigate(options: Options = None, arg: A) {
    val screen: () -> T by di.provider(arg = arg)
    navigate(screen(), options)
}
