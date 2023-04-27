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

    fun navigate(route: Route)

    fun changeNavigationContext(context: RouteContext)

    operator fun get(route: Route): RouteRenderer?
}

val LocalRouter = compositionLocalOf<Router> { error("Router not defined") }

internal typealias Navigator = Navi<Route, RouteContext>

internal class NavigationRouter(
    internal val navigator: Navigator,
    private val routes: NavigationRoutes
) : Router {

    override fun navigateBack() {
        navigator.goBack()
    }

    override fun navigate(route: Route) = navigator.goTo(route, ADD_TO_STACK)

    override fun get(route: Route) = routes[route]

    fun current(): Route = navigator.state.currentDestination

    override fun changeNavigationContext(context: RouteContext) =
        navigator.changeContext(context)
}

@OptIn(ExperimentalNavigationApi::class)
@Composable
internal fun rememberRouter(routes: NavigationRoutes): Router {
    val navigator = rememberNavigator(initialContext = routes.initialContext)
    return remember { NavigationRouter(navigator, routes) }
}
