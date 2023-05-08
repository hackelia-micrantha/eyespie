package com.micrantha.bluebell.domain

import com.micrantha.bluebell.domain.flux.Flux
import com.micrantha.bluebell.domain.flux.FluxDispatcher
import com.micrantha.bluebell.domain.usecase.FormatDateTimeUseCase
import org.koin.dsl.module

internal fun bluebellDomain() = module {
    single { Flux(FluxDispatcher()) }

    factory { FormatDateTimeUseCase(get()) }
}
