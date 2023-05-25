package com.micrantha.skouter.data.account.model

@kotlinx.serialization.Serializable
data class AccountResponse(
    val id: String,
    val nodeId: String? = null,
    val user_id: String,
    val created_at: String,
    val first_name: String,
    val last_name: String,
    val nick_name: String,
    val email: String? = null,
    val total_score: Int,
    val last_location: Pair<Double, Double>? = null
)
