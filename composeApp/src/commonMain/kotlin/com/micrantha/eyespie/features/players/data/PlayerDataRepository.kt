package com.micrantha.eyespie.features.players.data

import com.micrantha.eyespie.features.players.data.mapping.PlayerDomainMapper
import com.micrantha.eyespie.features.players.data.source.PlayerRemoteSource
import com.micrantha.eyespie.features.players.domain.repository.PlayerRepository

class PlayerDataRepository(
    private val remoteSource: PlayerRemoteSource,
    private val mapper: PlayerDomainMapper
) : PlayerRepository {

    override suspend fun players() = remoteSource.players().map {
        it.map(mapper::list)
    }

    override suspend fun player(userId: String) = remoteSource.player(userId)
        .map(mapper::map)

    override suspend fun create(
        userId: String,
        firstName: String,
        lastName: String,
        nickName: String
    ) = remoteSource.create(userId, firstName, lastName, nickName).mapCatching {
        remoteSource.player(userId).getOrThrow()
    }.map(mapper::map)
}
