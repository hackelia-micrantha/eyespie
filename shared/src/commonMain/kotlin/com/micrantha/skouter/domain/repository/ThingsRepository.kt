package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.models.Clues
import com.micrantha.skouter.domain.models.Location
import com.micrantha.skouter.domain.models.ThingList
import kotlinx.coroutines.flow.Flow

interface ThingsRepository {

    suspend fun recognize(image: ByteArray, contentType: String): Result<Clues>

    fun nearby(
        playerID: String,
        location: Location.Point,
        distance: Double = 10.0
    ): Flow<ThingList>
}
