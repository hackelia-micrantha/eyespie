package com.micrantha.skouter.data.thing.model

import com.micrantha.skouter.graphql.PlayerNearbyThingsQuery
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class ThingData(
    val id: String? = null,
    val created_at: String? = null,
    val name: String,
    val imageUrl: String,
    val image: JsonObject? = null,
    val clues: JsonObject? = null,
    val guessed: Boolean? = null,
    val created_by: String,
    val location: JsonPrimitive? = null
)

typealias ThingRequest = ThingData
typealias ThingResponse = ThingData
typealias ThingListing = ThingData
typealias ThingNearby = PlayerNearbyThingsQuery.Node
