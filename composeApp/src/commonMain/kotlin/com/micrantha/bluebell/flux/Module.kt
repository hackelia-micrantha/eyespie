package com.micrantha.bluebell.flux

import com.micrantha.bluebell.arch.Dispatcher
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.delegate

internal fun bluebellFlux() = DI.Module(name = "Bluebell Flux") {

    bindSingleton { Flux(FluxDispatcher()) }
    delegate<Dispatcher>().to<Flux>()
}