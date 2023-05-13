package com.micrantha.skouter.data.game.source

import com.micrantha.skouter.data.game.mapping.GameDomainMapper
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.GameList
import io.github.aakira.napier.Napier

class GameRemoteSource(
    private val client: SupaClient,
    private val mapper: GameDomainMapper
) {
    suspend fun games(): Result<GameList> = try {
        val games = client.games().execute()
            .dataAssertNoErrors.games!!.edges!!.filterNotNull()
            .map {
                mapper(it.node)
            }
        Result.success(games)
    } catch (e: Throwable) {
        Napier.e("games", e)
        Result.failure(e)
    }

    suspend fun game(id: String): Result<Game> = try {
        val game = with(client.game(id).execute()) {
            dataAssertNoErrors.gameNode!!
        }
        Result.success(mapper(game.nodeId, game.onGame!!))
    } catch (e: Throwable) {
        Napier.e("game", e)
        Result.failure(e)
    }
}
