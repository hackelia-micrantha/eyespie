package com.micrantha.skouter.data.player

import com.micrantha.skouter.data.player.source.PlayerRemoteSource
import com.micrantha.skouter.domain.models.ThingList
import com.micrantha.skouter.domain.repository.PlayerRepository

class PlayerDataRepository(
    private val remoteSource: PlayerRemoteSource
) : PlayerRepository {

    override suspend fun things(playerID: String): Result<ThingList> = remoteSource.things(playerID)
}
