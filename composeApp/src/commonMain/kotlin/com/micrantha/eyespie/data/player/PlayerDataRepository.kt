package com.micrantha.eyespie.data.player

import com.micrantha.eyespie.data.player.mapping.PlayerDomainMapper
import com.micrantha.eyespie.data.player.source.PlayerRemoteSource
import com.micrantha.eyespie.domain.model.PlayerList
import com.micrantha.eyespie.domain.repository.PlayerRepository

class PlayerDataRepository(
    private val remoteSource: PlayerRemoteSource,
    private val mapper: PlayerDomainMapper
) : PlayerRepository {

    override suspend fun players(): Result<PlayerList> = remoteSource.players().map {
        it.map(mapper::list)
    }
}
