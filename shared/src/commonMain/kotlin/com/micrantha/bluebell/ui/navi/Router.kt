package com.micrantha.bluebell.ui.navi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.SingleNavigationContext
import com.chrynan.navigation.StackDuplicateContentStrategy.ADD_TO_STACK
import com.chrynan.navigation.compose.rememberNavigator
import com.chrynan.navigation.Navigator as Navi

interface Router {
    fun back()

    fun navigate(route: Route)

    operator fun get(route: Route): RouteRenderer?
}

internal typealias Navigator = Navi<Route, SingleNavigationContext<Route>>

internal class NavigationRouter(
    internal val navigator: Navigator,
    private val routes: NavigationRoutes
) :
    Router {

    override fun back() {
        navigator.goBack()
    }

    override fun navigate(route: Route) = navigator.goTo(route, ADD_TO_STACK)

    override fun get(route: Route) = routes[route]
}

@OptIn(ExperimentalNavigationApi::class)
@Composable
internal fun rememberRouter(routes: NavigationRoutes): Router {
    val navigator = rememberNavigator(routes.defaultRoute)
    return remember { NavigationRouter(navigator, routes) }
}
