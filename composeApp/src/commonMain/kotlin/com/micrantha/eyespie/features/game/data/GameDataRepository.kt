package com.micrantha.eyespie.features.game.data

import com.micrantha.eyespie.features.game.data.mapping.GameDomainMapper
import com.micrantha.eyespie.features.game.data.source.GameRemoteSource
import com.micrantha.eyespie.domain.repository.GameRepository as DomainRepository

class GameDataRepository(
    private val remoteSource: GameRemoteSource,
    private val mapper: GameDomainMapper
) : DomainRepository {
    override suspend fun games() = remoteSource.games().map { it.map(mapper::list) }
    override suspend fun game(id: String) = remoteSource.game(id).map(mapper::map)
}
