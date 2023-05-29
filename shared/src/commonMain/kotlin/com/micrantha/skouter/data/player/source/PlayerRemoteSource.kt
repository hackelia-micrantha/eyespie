package com.micrantha.skouter.data.player.source

import com.micrantha.skouter.data.client.SupaClient
import com.micrantha.skouter.data.player.model.PlayerResponse

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
