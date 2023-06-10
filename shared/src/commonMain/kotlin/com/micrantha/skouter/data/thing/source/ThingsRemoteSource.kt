package com.micrantha.skouter.data.thing.source

import com.micrantha.skouter.data.client.SupaClient
import com.micrantha.skouter.data.thing.model.NearbyRequest
import com.micrantha.skouter.data.thing.model.ThingListing
import com.micrantha.skouter.data.thing.model.ThingRequest
import com.micrantha.skouter.data.thing.model.ThingResponse
import io.github.aakira.napier.Napier

class ThingsRemoteSource(
    private val supaClient: SupaClient,
) {

    suspend fun save(data: ThingRequest) = try {
        val result = supaClient.things().insert(data).decodeList<ThingResponse>()
        Result.success(result.first())
    } catch (err: Throwable) {
        Napier.e("save thing", err)
        Result.failure(err)
    }

    suspend fun things(playerID: String) = try {
        val result = supaClient.things().select {
            eq("created_by", playerID)
        }.decodeList<ThingListing>()
        Result.success(result)
    } catch (err: Throwable) {
        Result.failure(err)
    }

    suspend fun nearby(request: NearbyRequest) = try {
        val res = supaClient.nearby(request).decodeList<ThingResponse>()
        Result.success(res)
    } catch (err: Throwable) {
        Result.failure(err)
    }

}
