package com.micrantha.eyespie.domain.entities

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class Player(
    override val id: String,
    override val createdAt: Instant,
    val name: Name,
    val email: String,
    val score: Score,
    val location: Location? = null,
) : Entity, Creatable {

    data class Name(
        val first: String,
        val last: String,
        val nick: String?,
    ) {
        override fun toString(): String {
            return nick ?: "$first $last"
        }
    }

    data class Score(
        val total: Int
    )

    data class Ref(
        val id: String,
        val name: String,
    )

    data class Listing(
        override val id: String,
        val nodeId: String,
        override val createdAt: Instant,
        val name: String,
        val score: Int,
    ) : Entity, Creatable
}

typealias PlayerList = List<Player.Listing>
