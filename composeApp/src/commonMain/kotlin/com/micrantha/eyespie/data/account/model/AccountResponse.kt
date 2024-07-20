package com.micrantha.eyespie.data.account.model

import com.micrantha.eyespie.data.player.model.PlayerResponse

data class AccountResponse(
    val accessToken: String,
    val refreshToken: String,
    val player: PlayerResponse
)
