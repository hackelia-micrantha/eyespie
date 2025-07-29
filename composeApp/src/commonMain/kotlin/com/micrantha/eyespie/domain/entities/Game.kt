package com.micrantha.eyespie.domain.entities

import com.micrantha.eyespie.features.players.domain.entities.Player
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class Game(
    override val id: String,
    override val createdAt: Instant,
    val name: String,
    val expires: Instant,
    val limits: Limits,
    val turnDuration: Duration,
    val things: List<Thing.Listing>,
    val players: List<Player.Listing>
) : Entity, Creatable {

    data class Limits(
        val player: IntRange,
        val thing: IntRange
    )

    data class Listing(
        override val id: String,
        val nodeId: String,
        override val createdAt: Instant,
        val name: String,
        val expiresAt: Instant,
        val totalThings: Int,
        val totalPlayers: Int
    ) : Entity, Creatable

}

typealias GameList = List<Game.Listing>
