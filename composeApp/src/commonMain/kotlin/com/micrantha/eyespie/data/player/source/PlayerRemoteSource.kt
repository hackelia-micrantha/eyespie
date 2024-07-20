package com.micrantha.eyespie.data.player.source

import com.micrantha.eyespie.data.client.SupaClient
import com.micrantha.eyespie.data.player.model.PlayerResponse

class PlayerRemoteSource(
    private val supaClient: SupaClient
) {
    suspend fun players() = try {
        val result = supaClient.players().select()
            .decodeList<PlayerResponse>()
        Result.success(result)
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
