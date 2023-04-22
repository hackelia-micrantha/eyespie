package com.micrantha.bluebell.ui.navi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.SingleNavigationContext
import com.chrynan.navigation.StackDuplicateContentStrategy.ADD_TO_STACK
import com.chrynan.navigation.compose.rememberNavigator
import com.chrynan.navigation.Navigator as Navi

public interface Router {
    fun back()

    fun navigate(route: Route)
}

internal typealias Navigator = Navi<Route, SingleNavigationContext<Route>>

internal class NavigationRouter(internal val navigator: Navigator) :
    Router {

    override fun back() {
        navigator.goBack()
    }

    override fun navigate(route: Route) = navigator.goTo(route, ADD_TO_STACK)
}

@OptIn(ExperimentalNavigationApi::class)
@Composable
internal fun rememberRouter(defaultRoute: Route): Router {
    val navigator = rememberNavigator(defaultRoute)
    return remember { NavigationRouter(navigator) }
}
