package com.micrantha.skouter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.micrantha.bluebell.ui.navi.NavigationRoutes
import com.micrantha.bluebell.ui.navi.rememberRouter
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.MainScreen
import com.micrantha.skouter.ui.MainViewModel
import com.micrantha.skouter.ui.login.LoginScreen
import com.micrantha.skouter.ui.navi.routes
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf


@Composable
fun BluebellApp(
    koin: Koin,
    routes: NavigationRoutes,
    screen: @Composable (MainViewModel) -> Unit
) {
    val router = rememberRouter(routes)

    val viewContext: ViewContext by koin.inject { parametersOf(router) }

    val viewModel: MainViewModel by koin.inject { parametersOf(viewContext) }

    screen(viewModel)
}

@Composable
fun App(koin: Koin) {
    BluebellApp(koin, routes()) { viewModel ->

        val state by viewModel.state().collectAsState()

        if (state.isLoggedIn) {
            MainScreen(state, viewModel.viewContext)
        } else {
            LoginScreen(state, viewModel::dispatch)
        }
    }
}
