package com.micrantha.skouter.data.game

import com.micrantha.skouter.data.game.source.GameRemoteSource
import com.micrantha.skouter.domain.repository.GameRepository as DomainRepository

class GameDataRepository(
    private val remoteSource: GameRemoteSource
) : DomainRepository {
    override suspend fun games() = remoteSource.games()
    override suspend fun game(id: String) = remoteSource.game(id)
}
