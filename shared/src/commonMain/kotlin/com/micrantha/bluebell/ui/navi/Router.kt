package com.micrantha.bluebell.ui.navi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.StackDuplicateContentStrategy.ADD_TO_STACK
import com.chrynan.navigation.compose.rememberNavigator
import com.chrynan.navigation.Navigator as Navi

interface Router {
    fun navigateBack()

    fun canGoBack(): Boolean

    fun navigate(route: Route, vararg params: Any)

    fun changeNavigationContext(context: RouteContext)

    val current: Route?

    operator fun get(route: Route): Pair<RouteRenderer, Array<out Any>?>?
}

val LocalRouter = compositionLocalOf<Router> { error("Router not defined") }

internal typealias Navigator = Navi<Route, RouteContext>

internal class NavigationRouter(
    internal val navigator: Navigator,
    private val routes: NavigationRoutes
) : Router {

    private val params = mutableMapOf<Route, Array<out Any>?>()

    override fun navigateBack() {
        navigator.goBack()
    }

    override fun navigate(route: Route, vararg params: Any) {
        this.params[route] = params
        navigator.goTo(route, ADD_TO_STACK)
    }

    override fun get(route: Route) = routes[route]?.let { Pair(it, params[route]) }

    override val current: Route = navigator.state.currentDestination

    override fun canGoBack(): Boolean = navigator.canGoBack()

    override fun changeNavigationContext(context: RouteContext) =
        navigator.changeContext(context)
}

@OptIn(ExperimentalNavigationApi::class)
@Composable
internal fun rememberRouter(routes: NavigationRoutes): Router {
    val navigator = rememberNavigator(initialContext = routes.initialContext)
    return remember { NavigationRouter(navigator, routes) }
}
