package com.micrantha.eyespie.features.things

import com.micrantha.eyespie.features.things.data.ThingDataRepository
import com.micrantha.eyespie.features.things.data.mapping.ThingsDomainMapper
import com.micrantha.eyespie.features.things.data.source.ThingsRemoteSource
import com.micrantha.eyespie.features.things.ui.detail.ThingDetailEnvironment
import com.micrantha.eyespie.features.things.ui.detail.ThingDetailScreen
import com.micrantha.eyespie.features.things.ui.detail.ThingDetailScreenModel
import org.kodein.di.DI
import org.kodein.di.bindProviderOf


internal fun module() = DI.Module("Things") {
    bindProviderOf(::ThingDataRepository)
    bindProviderOf(::ThingsRemoteSource)
    bindProviderOf(::ThingsDomainMapper)

    bindProviderOf(::ThingDetailScreen)
    bindProviderOf(::ThingDetailScreenModel)
    bindProviderOf(::ThingDetailEnvironment)
}
