package com.micrantha.eyespie.features.things.data.source

import com.micrantha.bluebell.app.Log
import com.micrantha.eyespie.core.data.client.SupaClient
import com.micrantha.eyespie.features.things.data.model.MatchRequest
import com.micrantha.eyespie.features.things.data.model.MatchResponse
import com.micrantha.eyespie.features.things.data.model.NearbyRequest
import com.micrantha.eyespie.features.things.data.model.ThingListing
import com.micrantha.eyespie.features.things.data.model.ThingRequest
import com.micrantha.eyespie.features.things.data.model.ThingResponse

class ThingsRemoteSource(
    private val supaClient: SupaClient,
) {

    suspend fun save(data: ThingRequest) = try {
        val result = supaClient.things().insert(data).decodeList<ThingResponse>()
        Result.success(result.first())
    } catch (err: Throwable) {
        Log.e("save thing", err)
        Result.failure(err)
    }

    suspend fun things(playerID: String) = try {
        val result = supaClient.things().select {
            filter {
                eq("created_by", playerID)
            }
        }.decodeList<ThingListing>()
        Result.success(result)
    } catch (err: Throwable) {
        Result.failure(err)
    }

    suspend fun thing(thingID: String) = try {
        val result = supaClient.things().select {
            filter {
                eq("id", thingID)
            }
        }.decodeSingle<ThingResponse>()
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

    suspend fun match(request: MatchRequest) = try {
        val res = supaClient.match(request).decodeList<MatchResponse>()
        Result.success(res)
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
