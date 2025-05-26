package com.micrantha.eyespie.features.players.data

import com.micrantha.eyespie.domain.entities.PlayerList
import com.micrantha.eyespie.domain.repository.PlayerRepository
import com.micrantha.eyespie.features.players.data.mapping.PlayerDomainMapper
import com.micrantha.eyespie.features.players.data.source.PlayerRemoteSource

class PlayerDataRepository(
    private val remoteSource: PlayerRemoteSource,
    private val mapper: PlayerDomainMapper
) : PlayerRepository {

    override suspend fun players(): Result<PlayerList> = remoteSource.players().map {
        it.map(mapper::list)
    }
}
