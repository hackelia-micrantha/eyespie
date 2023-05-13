package com.micrantha.skouter.domain.models

import kotlinx.datetime.Instant

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

    data class Ref(
        val id: String,
        val name: String,
    )

    data class Listing(
        override val id: String,
        override val nodeId: String,
        override val createdAt: Instant,
        val name: String,
        val image: Image? = null,
        val score: Int,
    ) : Entity, Creatable
}

typealias PlayerList = List<Player.Listing>
