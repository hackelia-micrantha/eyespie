package com.micrantha.skouter.data.player.source

import com.micrantha.skouter.data.player.mapping.PlayerDomainMapper
import com.micrantha.skouter.data.player.model.PlayerResponse
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.domain.models.PlayerList

class PlayerRemoteSource(
    private val supaClient: SupaClient,
    private val mapper: PlayerDomainMapper
) {
    suspend fun players(): Result<PlayerList> = try {
        val result = supaClient.players().select()
            .decodeList<PlayerResponse>()
        Result.success(mapper.map(result))
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
