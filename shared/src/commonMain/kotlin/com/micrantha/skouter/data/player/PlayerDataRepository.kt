package com.micrantha.skouter.data.player

import com.micrantha.skouter.data.player.source.PlayerRemoteSource
import com.micrantha.skouter.domain.model.PlayerList
import com.micrantha.skouter.domain.repository.PlayerRepository

class PlayerDataRepository(
    private val remoteSource: PlayerRemoteSource
) : PlayerRepository {

    override suspend fun players(): Result<PlayerList> = remoteSource.players()
}
