package com.micrantha.eyespie.domain.model

data class Session(
    val accessToken: String,
    val refreshToken: String,
    val player: Player
)
