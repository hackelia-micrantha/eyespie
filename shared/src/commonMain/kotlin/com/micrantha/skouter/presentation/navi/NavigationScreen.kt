package com.micrantha.bluebell.ui.navi

import androidx.compose.runtime.Composable
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.compose.NavContainer
import com.micrantha.bluebell.ui.err.fail
import com.micrantha.bluebell.ui.view.rememberViewContext

@OptIn(ExperimentalNavigationApi::class)
@Composable
fun NavigationScreen(
    routes: NavigationRoutes,
) {
    val router = rememberRouter(routes.defaultRoute)

    val viewContext = rememberViewContext(router)

    NavContainer(navigator = router.navigator) { _, route ->
        routes[route]?.let { screen -> screen(viewContext) } ?: fail(
            "no screen for navigation to $route"
        )
    }
}
