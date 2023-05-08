package com.micrantha.skouter.data

import com.micrantha.skouter.data.accounts.source.AccountRemoteSource
import com.micrantha.skouter.data.games.mapping.GameDomainMapper
import com.micrantha.skouter.data.games.source.GameRemoteSource
import com.micrantha.skouter.data.remote.MicranthaClient
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.data.things.mapping.ThingsDomainMapper
import com.micrantha.skouter.data.things.source.ThingsRemoteSource
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.domain.repository.GameRepository
import com.micrantha.skouter.domain.repository.ThingsRepository
import org.koin.dsl.module
import com.micrantha.skouter.data.accounts.AccountRepository as AccountDataRepository
import com.micrantha.skouter.data.games.GameRepository as GameDataRepository
import com.micrantha.skouter.data.things.ThingsRepository as ThingDataRepository

internal fun dataModules() = module {
    single { SupaClient() }

    single { MicranthaClient() }

    factory { GameDomainMapper() }

    factory { ThingsDomainMapper() }

    factory { GameRemoteSource(get(), get()) }

    factory { ThingsRemoteSource(get(), get(), get()) }

    factory { AccountRemoteSource(get()) }

    factory<GameRepository> { GameDataRepository(get()) }

    factory<AccountRepository> { AccountDataRepository(get()) }

    factory<ThingsRepository> { ThingDataRepository(get()) }
}
