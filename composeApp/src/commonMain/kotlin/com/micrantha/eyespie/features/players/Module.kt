package com.micrantha.eyespie.features.players

import com.micrantha.bluebell.get
import com.micrantha.eyespie.features.players.data.PlayerDataRepository
import com.micrantha.eyespie.features.players.data.mapping.PlayerDomainMapper
import com.micrantha.eyespie.features.players.data.source.PlayerRemoteSource
import com.micrantha.eyespie.features.players.domain.usecase.LoadSessionPlayerUseCase
import com.micrantha.eyespie.features.players.ui.create.NewPlayerEnvironment
import com.micrantha.eyespie.features.players.ui.create.NewPlayerScreen
import com.micrantha.eyespie.features.players.ui.create.NewPlayerScreenModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf


internal fun module() = DI.Module("Players") {
    bindProviderOf(::PlayerDomainMapper)
    bindProviderOf(::PlayerDataRepository)
    bindProviderOf(::PlayerRemoteSource)
    bindProviderOf(::LoadSessionPlayerUseCase)
    bindProviderOf(::NewPlayerEnvironment)
    bindProvider { NewPlayerScreenModel(get(), get()) }
    bindProviderOf(::NewPlayerScreen)
}
