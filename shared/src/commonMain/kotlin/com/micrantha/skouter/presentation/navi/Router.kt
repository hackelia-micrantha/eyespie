package com.micrantha.bluebell.ui.navi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.Navigator
import com.chrynan.navigation.SingleNavigationContext
import com.chrynan.navigation.StackDuplicateContentStrategy.ADD_TO_STACK
import com.chrynan.navigation.compose.rememberNavigator

class Router(internal val navigator: Navigator<Route, SingleNavigationContext<Route>>) {

    fun back() = navigator.goBack()

    fun navigate(route: Route) = navigator.goTo(route, ADD_TO_STACK)
}

@OptIn(ExperimentalNavigationApi::class)
@Composable
fun rememberRouter(defaultRoute: Route): Router {
    val navigator = rememberNavigator(defaultRoute)
    return remember { Router(navigator) }
}
