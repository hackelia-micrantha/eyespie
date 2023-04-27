package com.micrantha.bluebell.ui.navi

import androidx.compose.runtime.Composable
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.compose.NavContainer
import com.micrantha.bluebell.data.err.fail
import com.micrantha.bluebell.ui.view.ViewContext

@OptIn(ExperimentalNavigationApi::class)
@Composable
internal fun NavigationScreen(
    viewContext: ViewContext
) {
    val router = LocalRouter.current as NavigationRouter

    NavContainer(navigator = router.navigator) { _, route ->
        router[route]?.let { screen -> screen(viewContext) } ?: fail(
            "no screen for navigation to $route"
        )
    }
}
