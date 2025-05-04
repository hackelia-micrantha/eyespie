package com.micrantha.eyespie.domain.entities

data class Session(
    val accessToken: String,
    val refreshToken: String,
    val player: Player
)
