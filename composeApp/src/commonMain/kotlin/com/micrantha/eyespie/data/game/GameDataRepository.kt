package com.micrantha.eyespie.data.game

import com.micrantha.eyespie.data.game.mapping.GameDomainMapper
import com.micrantha.eyespie.data.game.source.GameRemoteSource
import com.micrantha.eyespie.domain.repository.GameRepository as DomainRepository

class GameDataRepository(
    private val remoteSource: GameRemoteSource,
    private val mapper: GameDomainMapper
) : DomainRepository {
    override suspend fun games() = remoteSource.games().map { it.map(mapper::list) }
    override suspend fun game(id: String) = remoteSource.game(id).map(mapper::map)
}
