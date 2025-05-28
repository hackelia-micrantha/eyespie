package com.micrantha.eyespie.domain.repository

import com.micrantha.eyespie.domain.entities.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun currentLocation(): Location?

    fun flow(): Flow<Location>

    suspend fun start()

    fun stop()
}
