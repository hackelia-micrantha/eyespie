package com.micrantha.eyespie.features.players.data.source

import com.micrantha.eyespie.core.data.client.SupaClient
import com.micrantha.eyespie.features.players.data.model.PlayerResponse

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
