package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.model.Clues
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.domain.model.ThingList
import kotlinx.coroutines.flow.Flow

interface ThingsRepository {

    suspend fun recognize(image: ByteArray, contentType: String): Result<Clues>

    suspend fun things(playerID: String): Result<ThingList>

    suspend fun create(name: String, url: String, playerID: String): Result<Thing>

    fun nearby(
        playerID: String,
        location: Location.Point,
        distance: Double = 10.0
    ): Flow<ThingList>
}
