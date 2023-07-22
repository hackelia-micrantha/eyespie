package com.micrantha.skouter.data.thing

import com.micrantha.skouter.data.thing.mapping.ThingsDomainMapper
import com.micrantha.skouter.data.thing.source.ThingsRemoteSource
import com.micrantha.skouter.domain.model.Location.Point
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.platform.scan.model.ImageEmbedding
import com.micrantha.skouter.domain.repository.ThingRepository as DomainRepository

class ThingDataRepository(
    private val remoteSource: ThingsRemoteSource,
    private val mapper: ThingsDomainMapper
) : DomainRepository {

    override suspend fun things(playerID: String) = remoteSource.things(playerID).map {
        it.map(mapper::list)
    }

    override suspend fun thing(thingID: String) = remoteSource.thing(thingID)
        .map(mapper::map)

    override suspend fun create(
        proof: Proof
    ): Result<Thing> =
        remoteSource.save(mapper.new(proof)).map(mapper::map)

    override suspend fun nearby(
        location: Point,
        distance: Double
    ) = remoteSource.nearby(mapper.nearby(location, distance)).map {
        it.map(mapper::list)
    }

    override suspend fun match(embedding: ImageEmbedding) = remoteSource
        .match(mapper.match(embedding)).map {
            it.map(mapper::match)
        }
}
