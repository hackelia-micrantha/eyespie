package com.micrantha.bluebell.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.micrantha.bluebell.Platform
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.StoreFactory
import com.micrantha.bluebell.domain.flux.Flux
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.ui.navi.Router
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

interface ViewContext : StoreFactory, Dispatcher, LocalizedRepository, Router, KoinComponent

@Composable
inline fun rememberViewContext(koin: KoinComponent, router: Router): ViewContext {
    val viewContext = koin.get<ViewContext> { parametersOf(router) }
    return remember { viewContext }
}

class DefaultViewContext(
    private val router: Router,
    private val flux: Flux,
    internal val platform: Platform
) : StoreFactory by flux, Dispatcher by flux, LocalizedRepository by platform, Router by router,
    ViewContext

val LocalViewContext = compositionLocalOf<ViewContext> {
    error("View context not defined")
}
