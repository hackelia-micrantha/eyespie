package com.micrantha.skouter.data.games

import com.micrantha.skouter.data.games.source.GameRemoteSource
import com.micrantha.skouter.domain.repository.GameRepository as DomainRepository

class GameRepository(
    private val remoteSource: GameRemoteSource
) : DomainRepository {
    override suspend fun games() = remoteSource.games()
}
