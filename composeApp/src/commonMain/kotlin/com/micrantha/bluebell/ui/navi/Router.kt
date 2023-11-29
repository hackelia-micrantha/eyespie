package com.micrantha.bluebell.ui.navi

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.components.Router.Options.Replace
import com.micrantha.bluebell.ui.components.Router.Options.Reset

class BluebellRouter(
    private val navigator: Navigator
) : Router {

    override fun navigateBack() = navigator.pop()

    override val canGoBack: Boolean = navigator.canPop

    override val screen: Screen = navigator.lastItem

    override fun <T : Screen> navigate(screen: T, options: Router.Options) {
        when (options) {
            Replace -> navigator.replace(screen)
            Reset -> navigator.replaceAll(screen)
            else -> navigator.push(screen)
        }
    }
}
