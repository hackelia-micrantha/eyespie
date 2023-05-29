package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun currentLocation(): Location?

    fun locations(): Flow<Location>
}
