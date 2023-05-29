package com.micrantha.skouter.data.thing

import com.micrantha.skouter.data.thing.mapping.ThingsDomainMapper
import com.micrantha.skouter.data.thing.source.ThingsRemoteSource
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.domain.repository.ThingsRepository as DomainRepository

class ThingDataRepository(
    private val remoteSource: ThingsRemoteSource,
    private val mapper: ThingsDomainMapper
) : DomainRepository {

    override suspend fun things(playerID: String) = remoteSource.things(playerID).map {
        it.map(mapper::list)
    }

    override suspend fun create(
        name: String,
        url: String,
        proof: Proof,
        playerID: String
    ): Result<Thing> =
        remoteSource.save(mapper.new(name, url, proof, playerID)).map(mapper::map)

    override suspend fun nearby(
        location: Location.Point,
        distance: Double
    ) = remoteSource.nearby(mapper.nearby(location, distance)).map {
        it.map(mapper::list)
    }
}
