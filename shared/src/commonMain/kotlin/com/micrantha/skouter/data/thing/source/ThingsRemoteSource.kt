package com.micrantha.skouter.data.thing.source

import com.micrantha.skouter.data.remote.MicranthaClient
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.data.thing.mapping.ThingsDomainMapper
import com.micrantha.skouter.data.thing.model.RecognitionResponse
import com.micrantha.skouter.domain.models.Clues
import com.micrantha.skouter.domain.models.Location
import com.micrantha.skouter.domain.models.ThingList
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
        Result.success(mapper(response))
    } catch (err: Throwable) {
        Napier.e("recognize", err)
        Result.failure(err)
    }

    fun nearby(
        playerID: String,
        location: Location.Point,
        distance: Double
    ): Flow<ThingList> =
        supaClient.nearby(playerID, location.latitude, location.longitude, distance)
            .toFlow()
            .map { mapper(it.dataAssertNoErrors.searchThingsNearPlayer!!.edgesFilterNotNull()) }

}
