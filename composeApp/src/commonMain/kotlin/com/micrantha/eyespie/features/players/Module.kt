package com.micrantha.eyespie.features.players

import com.micrantha.eyespie.features.players.data.PlayerDataRepository
import com.micrantha.eyespie.features.players.data.mapping.PlayerDomainMapper
import com.micrantha.eyespie.features.players.data.source.PlayerRemoteSource
import org.kodein.di.DI
import org.kodein.di.bindProviderOf


internal fun module() = DI.Module("Players") {
    bindProviderOf(::PlayerDomainMapper)
    bindProviderOf(::PlayerDataRepository)
    bindProviderOf(::PlayerRemoteSource)
}
