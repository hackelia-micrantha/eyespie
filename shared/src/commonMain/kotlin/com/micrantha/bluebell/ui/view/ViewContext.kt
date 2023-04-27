package com.micrantha.bluebell.ui.view

import androidx.compose.runtime.compositionLocalOf
import com.micrantha.bluebell.Platform
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.StoreFactory
import com.micrantha.bluebell.domain.flux.Flux
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.ui.navi.Router

interface ViewContext : StoreFactory, Dispatcher, LocalizedRepository, Router

class DefaultViewContext(
    internal val router: Router,
    private val flux: Flux,
    internal val platform: Platform
) : StoreFactory by flux, Dispatcher by flux, LocalizedRepository by platform, Router by router,
    ViewContext

val LocalViewContext = compositionLocalOf<ViewContext> {
    error("View context not defined")
}
