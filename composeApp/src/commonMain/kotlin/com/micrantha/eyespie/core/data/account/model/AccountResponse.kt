package com.micrantha.eyespie.core.data.account.model

import com.micrantha.eyespie.features.players.data.model.PlayerResponse

data class AccountResponse(
    val accessToken: String,
    val refreshToken: String,
    val player: PlayerResponse
)
