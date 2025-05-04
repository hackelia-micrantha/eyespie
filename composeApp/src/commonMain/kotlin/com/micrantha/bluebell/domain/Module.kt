package com.micrantha.bluebell.domain

import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.flux.Flux
import com.micrantha.bluebell.flux.FluxDispatcher
import com.micrantha.bluebell.domain.usecase.FormatDateTimeUseCase
import org.kodein.di.DI
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingleton
import org.kodein.di.delegate

internal fun bluebellDomain() = DI.Module(name = "Bluebell Domain") {

    bindSingleton { Flux(FluxDispatcher()) }

    delegate<Dispatcher>().to<Flux>()

    bindProviderOf(::FormatDateTimeUseCase)
}
