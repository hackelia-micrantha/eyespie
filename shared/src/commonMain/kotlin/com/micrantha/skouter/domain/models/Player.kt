package com.micrantha.skouter.domain.models

import kotlinx.datetime.Instant

data class PlayerListing(
    override val id: String,
    override val nodeId: String,
    override val createdAt: Instant,
    val name: String,
    val image: String
) : Entity, Creatable

data class Player(
    override val id: String,
    override val nodeId: String,
    override val createdAt: Instant,
    val name: Name,
    val email: String,
    val score: Score,
    val image: Image? = null,
) : Entity, Creatable {

    data class Name(
        val first: String,
        val last: String,
        val nick: String,
    )

    data class Score(
        val total: Int
    )

    data class Image(
        val small: String,
        val medium: String,
        val large: String,
    )

    data class Ref(
        val id: String,
        val name: String,
    )
}
