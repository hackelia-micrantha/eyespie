package com.micrantha.eyespie.data.player.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class PlayerResponse(
    val id: String,
    val nodeId: String? = null,
    val user_id: String,
    val created_at: String,
    val first_name: String,
    val last_name: String,
    val nick_name: String? = null,
    val email: String? = null,
    val total_score: Int,
    val last_location: JsonElement? = null
)
