package com.micrantha.skouter.data.thing.source

import com.micrantha.skouter.data.remote.MicranthaClient
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.data.thing.mapping.ThingsDomainMapper
import com.micrantha.skouter.data.thing.model.RecognitionResponse
import com.micrantha.skouter.data.thing.model.ThingResponse
import com.micrantha.skouter.domain.model.Clues
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.ThingList
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThingsRemoteSource(
    private val micranthaClient: MicranthaClient,
    private val supaClient: SupaClient,
    private val mapper: ThingsDomainMapper
) {
    suspend fun recognize(image: ByteArray, contentType: String): Result<Clues> = try {
        val response: RecognitionResponse = micranthaClient.recognize(image, contentType).body()
        Result.success(mapper.map(response))
    } catch (err: Throwable) {
        Napier.e("recognize", err)
        Result.failure(err)
    }

    suspend fun things(playerID: String): Result<ThingList> = try {
        val result = supaClient.things().select {
            eq("created_by", playerID)
        }.decodeList<ThingResponse>()
        Result.success(result.map(mapper::map))
    } catch (err: Throwable) {
        Result.failure(err)
    }

    fun nearby(
        playerID: String,
        location: Location.Point,
        distance: Double
    ): Flow<ThingList> =
        supaClient.nearby(playerID, location.latitude, location.longitude, distance)
            .toFlow()
            .map { data ->
                data.dataAssertNoErrors.searchThingsNearPlayer!!.edgesFilterNotNull()
                    ?.map { it.node }
                    ?.map(mapper::map) ?: emptyList()
            }

}
