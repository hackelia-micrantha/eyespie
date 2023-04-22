package com.micrantha.skouter.data

import com.micrantha.skouter.data.accounts.source.AccountRemoteSource
import com.micrantha.skouter.data.games.source.GameRemoteSource
import com.micrantha.skouter.data.remote.ApiClient
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.domain.repository.GameRepository
import org.koin.dsl.module
import com.micrantha.skouter.data.accounts.AccountRepository as AccountRepositoryImpl
import com.micrantha.skouter.data.games.GameRepository as GameRepositoryImpl

internal fun dataModules() = module {
    single { ApiClient() }

    factory { GameRemoteSource(get()) }

    factory { AccountRemoteSource(get()) }

    factory<GameRepository> { GameRepositoryImpl(get()) }

    factory<AccountRepository> { AccountRepositoryImpl(get()) }
}
