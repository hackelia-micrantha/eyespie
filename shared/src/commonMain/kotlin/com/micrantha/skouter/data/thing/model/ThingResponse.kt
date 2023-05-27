package com.micrantha.skouter.data.thing.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class ThingResponse(
    val id: String,
    val created_at: String,
    val name: String,
    val imageUrl: String,
    val image: JsonObject? = null,
    val clues: JsonObject? = null,
    val guessed: Boolean? = null,
    val created_by: String,
    val location: JsonPrimitive? = null
) {
    @Serializable
    data class Point(
        val latitude: Double,
        val longitude: Double
    )
}
