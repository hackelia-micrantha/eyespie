package com.micrantha.bluebell

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.micrantha.bluebell.ui.MainScreen
import com.micrantha.bluebell.ui.MainViewModel
import com.micrantha.bluebell.ui.navi.NavigationRoutes
import com.micrantha.bluebell.ui.navi.rememberRouter
import com.micrantha.bluebell.ui.view.ViewContext
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf

@Composable
fun BluebellApp(koin: Koin, routes: NavigationRoutes) {
    val router = rememberRouter(routes.defaultRoute)

    val viewContext: ViewContext by koin.inject { parametersOf(router) }

    val viewModel: MainViewModel by koin.inject { parametersOf(viewContext) }

    Surface(color = MaterialTheme.colorScheme.background) {
        MainScreen(viewModel, routes)
    }
}
