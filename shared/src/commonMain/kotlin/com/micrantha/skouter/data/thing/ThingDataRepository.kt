package com.micrantha.skouter.data.thing

import com.micrantha.skouter.data.thing.mapping.ThingsDomainMapper
import com.micrantha.skouter.data.thing.source.ThingsRemoteSource
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.Thing
import kotlinx.coroutines.flow.map
import com.micrantha.skouter.domain.repository.ThingsRepository as DomainRepository

class ThingDataRepository(
    private val remoteSource: ThingsRemoteSource,
    private val mapper: ThingsDomainMapper
) : DomainRepository {

    override suspend fun things(playerID: String) = remoteSource.things(playerID).map {
        it.map(mapper::list)
    }

    override suspend fun create(name: String, url: String, playerID: String): Result<Thing> =
        remoteSource.save(mapper.new(name, url, playerID)).map(mapper::map)

    override fun nearby(
        playerID: String,
        location: Location.Point,
        distance: Double
    ) = remoteSource.nearby(playerID, location, distance).map { it.map(mapper::nearby) }
}
