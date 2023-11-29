package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun currentLocation(): Location?

    fun flow(): Flow<Location>

    suspend fun start()

    fun stop()
}
