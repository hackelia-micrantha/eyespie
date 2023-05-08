package com.micrantha.skouter.domain.models

import com.micrantha.skouter.domain.models.Thing.Image
import kotlinx.datetime.Instant
import kotlin.time.Duration

data class GameListing(
    override val id: String,
    override val nodeId: String,
    override val createdAt: Instant,
    val name: String,
    val expiresAt: Instant,
    val totalThings: Int,
    val totalPlayers: Int
) : Entity, Creatable

data class Game(
    override val id: String,
    override val nodeId: String,
    override val createdAt: Instant,
    val name: String,
    val expires: Instant,
    val limits: Limits,
    val turnDuration: Duration,
    val things: List<Thing>,
    val players: List<Player>
) : Entity, Creatable {

    data class Limits(
        val player: IntRange,
        val thing: IntRange
    )

    data class Thing(
        override val id: String,
        override val nodeId: String,
        override val createdAt: Instant,
        val name: String,
        val guessed: Boolean,
        val image: Image?,
    ) : Entity, Creatable

    data class Player(
        override val id: String,
        override val nodeId: String,
        override val createdAt: Instant,
        val name: String,
        val score: Int,
    ) : Entity, Creatable
}
