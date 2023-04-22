package com.micrantha.bluebell.ui.view

import com.micrantha.bluebell.Platform
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.StoreFactory
import com.micrantha.bluebell.domain.flux.Flux
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.ui.navi.Router

class ViewContext(
    internal val router: Router,
    private val flux: Flux,
    private val platform: Platform
) : StoreFactory by flux, Dispatcher by flux, LocalizedRepository by platform, Router by router

