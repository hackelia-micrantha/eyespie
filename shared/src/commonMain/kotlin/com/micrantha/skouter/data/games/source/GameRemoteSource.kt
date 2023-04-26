package com.micrantha.skouter.data.games.source

import com.micrantha.skouter.data.games.mapping.GameDomainMapper
import com.micrantha.skouter.data.remote.ApiClient
import com.micrantha.skouter.domain.models.GameListing

class GameRemoteSource(
    private val client: ApiClient,
    private val mapper: GameDomainMapper = GameDomainMapper()
) {
    suspend fun games(): Result<List<GameListing>> = try {
        val games = client.games().execute()
            .dataAssertNoErrors.gamesCollection!!.edges!!.filterNotNull()
            .map {
                mapper(it.node!!)
            }
        Result.success(games)
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
