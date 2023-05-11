package com.micrantha.bluebell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import com.micrantha.bluebell.ui.components.effects.InjectDependencyEffect
import com.micrantha.bluebell.ui.navi.LocalRouter
import com.micrantha.bluebell.ui.navi.NavigationRoutes
import com.micrantha.bluebell.ui.navi.rememberRouter
import com.micrantha.bluebell.ui.scaffold.ScaffoldScreen
import com.micrantha.bluebell.ui.scaffold.rememberScaffoldState
import com.micrantha.bluebell.ui.view.LocalViewContext
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.bluebell.ui.view.ViewModel
import com.micrantha.bluebell.ui.view.rememberViewContext

class AppConfig {
    var platform: Platform? = null
    var routes: NavigationRoutes? = null
    var scaffolding: Boolean = false
    var mainViewModel: ViewModel? = null
}

@Composable
fun BluebellApp(
    platform: Platform,
    routes: NavigationRoutes,
    content: @Composable (ViewContext) -> Unit
) {
    val router = rememberRouter(routes)

    val viewContext = rememberViewContext(platform, router)

    val state by rememberScaffoldState(viewContext)

    InjectDependencyEffect(platform, viewContext)

    CompositionLocalProvider(
        LocalRouter provides router,
        LocalViewContext provides viewContext
    ) {
        ScaffoldScreen(viewContext, state) {
            content(viewContext)
        }
    }
}
