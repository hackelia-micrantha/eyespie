package com.micrantha.eyespie.features.things.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MatchRequest(
    @SerialName("query_embedding") val embedding: JsonElement,
    @SerialName("match_threshold") val threshold: Float,
    @SerialName("match_count") val count: Int
)

@Serializable
data class MatchResponse(
    val id: String,
    val content: JsonElement,
    val similarity: Float
)
