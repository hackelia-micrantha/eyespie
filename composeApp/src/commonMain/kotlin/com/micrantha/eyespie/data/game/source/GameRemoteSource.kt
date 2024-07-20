package com.micrantha.eyespie.data.game.source

import com.micrantha.bluebell.data.Log
import com.micrantha.eyespie.data.client.SupaClient

class GameRemoteSource(
    private val client: SupaClient
) {
    suspend fun games() = try {
        val games = client.games().execute()
            .dataAssertNoErrors.games!!.edges!!.filterNotNull()
            .map { it.node }
        Result.success(games)
    } catch (e: Throwable) {
        Log.e("games", e)
        Result.failure(e)
    }

    suspend fun game(id: String) = try {
        val game = with(client.game(id).execute()) {
            dataAssertNoErrors.gameNode!!
        }
        Result.success(game)
    } catch (e: Throwable) {
        Log.e("game", e)
        Result.failure(e)
    }
}