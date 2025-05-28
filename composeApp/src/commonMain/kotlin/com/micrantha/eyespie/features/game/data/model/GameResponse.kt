package com.micrantha.eyespie.features.game.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GameResponse(
    val id: String,
    val created_at: String,
    val expires: String,
    val name: String,
    val max_things: Int,
    val min_things: Int,
    val max_players: Int,
    val min_players: Int,
    val turn_duration: String
)
