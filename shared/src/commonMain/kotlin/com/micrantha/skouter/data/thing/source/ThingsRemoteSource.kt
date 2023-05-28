package com.micrantha.skouter.data.thing.source

import com.micrantha.skouter.data.remote.MicranthaClient
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.data.thing.model.ThingListing
import com.micrantha.skouter.data.thing.model.ThingNearby
import com.micrantha.skouter.data.thing.model.ThingRequest
import com.micrantha.skouter.data.thing.model.ThingResponse
import com.micrantha.skouter.domain.model.Location
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThingsRemoteSource(
    private val micranthaClient: MicranthaClient,
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

    fun nearby(
        playerID: String,
        location: Location.Point,
        distance: Double
    ): Flow<List<ThingNearby>> =
        supaClient.nearby(playerID, location.latitude, location.longitude, distance)
            .toFlow()
            .map { data ->
                data.dataAssertNoErrors.searchThingsNearPlayer!!.edgesFilterNotNull()
                    ?.map { it.node } ?: emptyList()
            }

}
