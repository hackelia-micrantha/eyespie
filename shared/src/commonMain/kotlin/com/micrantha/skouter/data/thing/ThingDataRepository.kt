package com.micrantha.skouter.data.thing

import com.micrantha.skouter.data.thing.source.ThingsRemoteSource
import com.micrantha.skouter.domain.models.Clues
import com.micrantha.skouter.domain.models.Location
import com.micrantha.skouter.domain.repository.ThingsRepository as DomainRepository

class ThingDataRepository(
    private val remoteSource: ThingsRemoteSource,
) : DomainRepository {
    override suspend fun recognize(image: ByteArray, contentType: String): Result<Clues> =
        remoteSource.recognize(image, contentType)

    override suspend fun things(playerID: String) = remoteSource.things(playerID)
    
    override fun nearby(
        playerID: String,
        location: Location.Point,
        distance: Double
    ) = remoteSource.nearby(playerID, location, distance)
}
