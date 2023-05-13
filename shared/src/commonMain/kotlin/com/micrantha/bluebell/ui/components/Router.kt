package com.micrantha.bluebell.ui.components

import cafe.adriel.voyager.core.screen.Screen
import com.micrantha.bluebell.ui.components.Router.Options.None

interface Router {
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
