package com.micrantha.bluebell.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.micrantha.bluebell.ui.Modules
import com.micrantha.bluebell.ui.arch.Dispatcher
import com.micrantha.bluebell.ui.arch.StoreFactory
import com.micrantha.bluebell.ui.flux.Flux
import com.micrantha.bluebell.ui.navi.Router
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class ViewContext(
    internal val router: Router,
    private val flux: Flux
) : StoreFactory by flux, Dispatcher by flux

@Composable
fun rememberViewContext(router: Router): ViewContext {
    val viewContext: ViewContext by Modules.inject { parametersOf(router) }
    return remember { viewContext }
}

