package com.micrantha.skouter.data.game.source

import com.micrantha.skouter.data.remote.SupaClient
import io.github.aakira.napier.Napier

class GameRemoteSource(
    private val client: SupaClient
) {
    suspend fun games() = try {
        val games = client.games().execute()
            .dataAssertNoErrors.games!!.edges!!.filterNotNull()
            .map { it.node }
        Result.success(games)
    } catch (e: Throwable) {
        Napier.e("games", e)
        Result.failure(e)
    }

    suspend fun game(id: String) = try {
        val game = with(client.game(id).execute()) {
            dataAssertNoErrors.gameNode!!
        }
        Result.success(game)
    } catch (e: Throwable) {
        Napier.e("game", e)
        Result.failure(e)
    }
}
