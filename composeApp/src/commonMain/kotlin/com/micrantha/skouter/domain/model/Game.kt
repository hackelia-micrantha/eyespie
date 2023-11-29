package com.micrantha.skouter.domain.model

import kotlinx.datetime.Instant
import kotlin.time.Duration

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
