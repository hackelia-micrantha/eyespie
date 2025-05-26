package com.micrantha.bluebell.domain

import com.micrantha.bluebell.domain.usecase.FormatDateTimeUseCase
import org.kodein.di.DI
import org.kodein.di.bindProviderOf

internal fun bluebellDomain() = DI.Module(name = "Bluebell Domain") {

    bindProviderOf(::FormatDateTimeUseCase)
}
