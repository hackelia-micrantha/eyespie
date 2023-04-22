package com.micrantha.skouter.domain.models

import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

data class GameListing(
    override val id: String,
    override val createdAt: LocalDateTime,
    val name: String,
    val expiresAt: LocalDateTime,
    val totalThings: Int,
    val totalPlayers: Int
) : Entity, Creatable

data class Game(
    override val id: String,
    override val createdAt: LocalDateTime,
    val name: String,
    val expires: LocalDateTime,
    val limits: Limits,
    val turnDuration: Duration,
    val things: List<Thing>,
    val players: List<Player.Ref>
) : Entity, Creatable {
    data class Limits(
        val player: IntRange,
        val thing: IntRange
    )
}
