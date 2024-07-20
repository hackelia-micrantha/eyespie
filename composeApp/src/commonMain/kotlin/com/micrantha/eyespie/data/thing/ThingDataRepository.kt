package com.micrantha.eyespie.data.thing

import com.micrantha.eyespie.data.thing.mapping.ThingsDomainMapper
import com.micrantha.eyespie.data.thing.source.ThingsRemoteSource
import com.micrantha.eyespie.domain.model.Embedding
import com.micrantha.eyespie.domain.model.Location.Point
import com.micrantha.eyespie.domain.model.Proof
import com.micrantha.eyespie.domain.model.Thing
import com.micrantha.eyespie.domain.repository.ThingRepository as DomainRepository

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

    override suspend fun match(embedding: Embedding) = remoteSource
        .match(mapper.match(embedding)).map {
            it.map(mapper::match)
        }
}
