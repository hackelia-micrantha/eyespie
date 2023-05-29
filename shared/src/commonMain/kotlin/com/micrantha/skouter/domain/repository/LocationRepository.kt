package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.model.Location

interface LocationRepository {

    fun currentLocation(): Location?

    suspend fun start()

    fun stop()
}
