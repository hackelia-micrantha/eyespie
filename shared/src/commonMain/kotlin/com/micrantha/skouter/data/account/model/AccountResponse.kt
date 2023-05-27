package com.micrantha.skouter.data.account.model

import com.micrantha.skouter.data.player.model.PlayerResponse

data class AccountResponse(
    val accessToken: String,
    val refreshToken: String,
    val player: PlayerResponse
)
