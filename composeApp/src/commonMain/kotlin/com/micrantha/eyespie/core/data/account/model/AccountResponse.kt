package com.micrantha.eyespie.core.data.account.model

data class AccountResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)
