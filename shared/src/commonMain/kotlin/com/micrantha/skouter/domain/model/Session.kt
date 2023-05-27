package com.micrantha.skouter.domain.model

data class Session(
    val accessToken: String,
    val refreshToken: String,
    val player: Player
)
