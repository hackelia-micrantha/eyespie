package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.domain.model.ThingList

interface ThingRepository {

    suspend fun things(playerID: String): Result<ThingList>

    suspend fun create(
        thing: Thing.Create,
        url: String,
        playerID: String,
        location: Location
    ): Result<Thing>

    suspend fun nearby(
        location: Location.Point,
        distance: Double = 10.0
    ): Result<ThingList>
}
