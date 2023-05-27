package com.micrantha.skouter.data.player

import com.micrantha.skouter.data.player.mapping.PlayerDomainMapper
import com.micrantha.skouter.data.player.source.PlayerRemoteSource
import com.micrantha.skouter.domain.model.PlayerList
import com.micrantha.skouter.domain.repository.PlayerRepository

class PlayerDataRepository(
    private val remoteSource: PlayerRemoteSource,
    private val mapper: PlayerDomainMapper
) : PlayerRepository {

    override suspend fun players(): Result<PlayerList> = remoteSource.players().map {
        it.map(mapper::list)
    }
}
