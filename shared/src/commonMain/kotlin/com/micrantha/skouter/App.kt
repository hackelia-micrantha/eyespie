package com.micrantha.skouter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.micrantha.bluebell.ui.navi.LocalRouter
import com.micrantha.bluebell.ui.navi.rememberRouter
import com.micrantha.bluebell.ui.view.LocalViewContext
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.MainScreen
import com.micrantha.skouter.ui.MainViewModel
import com.micrantha.skouter.ui.navi.routes
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf

@Composable
fun App(koin: Koin) {
    val router = rememberRouter(routes())

    val viewContext: ViewContext by koin.inject { parametersOf(router) }

    val viewModel: MainViewModel by koin.inject { parametersOf(viewContext) }

    val state by viewModel.state().collectAsState()

    CompositionLocalProvider(
        LocalRouter provides router,
        LocalViewContext provides viewContext,
    ) {
        MainScreen(viewContext, state)
    }
}
