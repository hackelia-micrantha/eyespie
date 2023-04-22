package com.micrantha.bluebell.ui.navi

import androidx.compose.runtime.Composable
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.compose.NavContainer
import com.micrantha.bluebell.data.err.fail
import com.micrantha.bluebell.ui.view.ViewContext

@OptIn(ExperimentalNavigationApi::class)
@Composable
internal fun NavigationScreen(
    routes: NavigationRoutes,
    viewContext: ViewContext,
) {
    NavContainer(navigator = viewContext.navigator()) { _, route ->
        routes[route]?.let { screen -> screen(viewContext) } ?: fail(
            "no screen for navigation to $route"
        )
    }
}

private fun ViewContext.navigator() = (this.router as NavigationRouter).navigator
