package com.micrantha.eyespie.features.game.data.mapping

import com.micrantha.eyespie.domain.entities.Game
import com.micrantha.eyespie.domain.entities.Game.Limits
import com.micrantha.eyespie.domain.entities.Player
import com.micrantha.eyespie.domain.entities.Thing
import com.micrantha.eyespie.graphql.GameListQuery
import com.micrantha.eyespie.graphql.GameNodeQuery
import com.micrantha.eyespie.graphql.GameNodeQuery.Node
import com.micrantha.eyespie.graphql.GameNodeQuery.Node2
import kotlin.time.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class GameDomainMapper {

    fun list(data: GameListQuery.Node) = Game.Listing(
        id = data.id,
        nodeId = data.nodeId,
        name = data.name,
        createdAt = Instant.parse(data.created_at),
        expiresAt = Instant.parse(data.expires),
        totalPlayers = data.players?.totalCount ?: 0,
        totalThings = data.things?.totalCount ?: 0
    )

    fun map(node: GameNodeQuery.GameNode) = node.onGame!!.let { data ->
        Game(
            id = data.id,
            name = data.name,
            createdAt = Instant.parse(data.created_at),
            expires = Instant.parse(data.expires),
            turnDuration = Duration.parse(data.turn_duration),
            players = data.players?.edges?.filterNotNull()?.map { it.node }?.map(::player)
                ?: emptyList(),
            things = data.things?.edges?.filterNotNull()?.map { it.node }?.map(::thing)
                ?: emptyList(),
            limits = Limits(
                player = IntRange(data.min_players ?: 1, data.max_players ?: 10),
                thing = IntRange(data.min_things ?: 1, data.max_things ?: 10)
            )
        )
    }

    private fun player(node: Node2): Player.Listing {
        val score = node.score ?: 0
        return node.player.let { data ->
            Player.Listing(
                id = data.id,
                nodeId = data.nodeId,
                createdAt = Instant.parse(data.created_at),
                name = "${data.first_name} ${data.last_name}",
                score = score
            )
        }
    }

    private fun thing(node: Node) = node.thing.let { data ->
        Thing.Listing(
            id = data.id,
            nodeId = data.nodeId,
            createdAt = Instant.parse(data.created_at),
            name = data.name,
            guessed = data.guessed == true,
            imageUrl = data.imageUrl
        )
    }
}
