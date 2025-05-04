package com.micrantha.eyespie.domain.repository

import com.micrantha.eyespie.domain.entities.Embedding
import com.micrantha.eyespie.domain.entities.Location
import com.micrantha.eyespie.domain.entities.Proof
import com.micrantha.eyespie.domain.entities.Thing
import com.micrantha.eyespie.domain.entities.ThingList
import com.micrantha.eyespie.domain.entities.ThingMatches

interface ThingRepository {

    suspend fun things(playerID: String): Result<ThingList>

    suspend fun thing(thingID: String): Result<Thing>

    suspend fun match(embedding: Embedding): Result<ThingMatches>

    suspend fun create(
        proof: Proof
    ): Result<Thing>

    suspend fun nearby(
        location: Location.Point,
        distance: Double = 10.0
    ): Result<ThingList>
}
