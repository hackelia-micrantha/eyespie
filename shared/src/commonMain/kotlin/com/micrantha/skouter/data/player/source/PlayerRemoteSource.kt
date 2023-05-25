package com.micrantha.skouter.data.player.source

import com.micrantha.skouter.data.player.mapping.PlayerDomainMapper
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.data.thing.model.ThingResponse
import com.micrantha.skouter.domain.models.ThingList

class PlayerRemoteSource(
    private val supaClient: SupaClient,
    private val mapper: PlayerDomainMapper
) {
    suspend fun things(playerID: String): Result<ThingList> = try {
        val result = supaClient.things().select {
            eq("created_by", playerID)
        }.decodeList<ThingResponse>()
        Result.success(mapper(result))
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
