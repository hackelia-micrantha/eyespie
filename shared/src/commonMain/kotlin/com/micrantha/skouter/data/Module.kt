package com.micrantha.skouter.data

import com.micrantha.skouter.data.accounts.source.AccountRemoteSource
import com.micrantha.skouter.data.games.mapping.GameDomainMapper
import com.micrantha.skouter.data.games.source.GameRemoteSource
import com.micrantha.skouter.data.remote.MicranthaClient
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.data.things.mapping.ThingsDomainMapper
import com.micrantha.skouter.data.things.source.ThingsRemoteSource
import org.kodein.di.DI
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingletonOf
import com.micrantha.skouter.data.accounts.AccountRepository as AccountDataRepository
import com.micrantha.skouter.data.games.GameRepository as GameDataRepository
import com.micrantha.skouter.data.things.ThingsRepository as ThingDataRepository

internal fun dataModules() = DI.Module("Skouter Data") {
    bindSingletonOf(::SupaClient)

    bindSingletonOf(::MicranthaClient)

    bindProviderOf(::GameDomainMapper)

    bindProviderOf(::ThingsDomainMapper)

    bindProviderOf(::GameRemoteSource)

    bindProviderOf(::ThingsRemoteSource)

    bindProviderOf(::AccountRemoteSource)

    bindProviderOf(::GameDataRepository)

    bindProviderOf(::AccountDataRepository)

    bindProviderOf(::ThingDataRepository)
}
