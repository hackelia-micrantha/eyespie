package com.micrantha.eyespie.core

import com.micrantha.eyespie.core.data.account.AccountDataRepository
import com.micrantha.eyespie.core.data.account.mapping.AccountDomainMapper
import com.micrantha.eyespie.core.data.account.model.CurrentSession
import com.micrantha.eyespie.core.data.account.source.AccountRemoteSource
import com.micrantha.eyespie.core.data.client.SupaClient
import com.micrantha.eyespie.core.data.client.SupaRealtimeClient
import com.micrantha.eyespie.core.data.storage.StorageDataRepository
import com.micrantha.eyespie.core.data.storage.source.StorageLocalSource
import com.micrantha.eyespie.core.data.storage.source.StorageRemoteSource
import com.micrantha.eyespie.core.data.system.LocationDataRepository
import com.micrantha.eyespie.core.data.system.RealtimeDataRepository
import com.micrantha.eyespie.core.data.system.mapping.LocationDomainMapper
import com.micrantha.eyespie.core.data.system.mapping.RealtimeDomainMapper
import com.micrantha.eyespie.core.data.system.source.LocationLocalSource
import com.micrantha.eyespie.core.data.system.source.RealtimeRemoteSource
import dev.icerock.moko.geo.LocationTracker
import org.kodein.di.DI
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.delegate

internal fun module() = DI.Module("Core Feature") {
    bindSingletonOf(::SupaClient)
    bindSingletonOf(::SupaRealtimeClient)

    bindProviderOf(::AccountRemoteSource)
    bindProviderOf(::AccountDataRepository)

    bindProviderOf(::AccountDomainMapper)

    bindSingleton { CurrentSession }

    bindProviderOf(::StorageLocalSource)
    bindProviderOf(::StorageRemoteSource)
    bindProviderOf(::StorageDataRepository)

    bindProviderOf(::LocationDomainMapper)
    bindProviderOf(::LocationDataRepository)
    bindProviderOf(::RealtimeDataRepository)
    bindSingletonOf(::RealtimeRemoteSource)
    bindProviderOf(::RealtimeDomainMapper)

    delegate<LocationLocalSource>().to<LocationTracker>()
}