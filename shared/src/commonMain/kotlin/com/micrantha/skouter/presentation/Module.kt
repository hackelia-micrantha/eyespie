package com.micrantha.bluebell.ui

import com.micrantha.bluebell.ui.flux.Flux
import com.micrantha.bluebell.ui.flux.FluxDispatcher
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.data.remote.ApiClient
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

internal object Modules : KoinComponent

fun loadUiModules() = loadKoinModules(module {
    single { Flux(FluxDispatcher()) }

    single { ApiClient() }

    factory { param -> ViewContext(param.get(), get()) }
})
