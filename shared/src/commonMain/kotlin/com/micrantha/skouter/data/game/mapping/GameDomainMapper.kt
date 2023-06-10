package com.micrantha.skouter.data.game.mapping

import com.micrantha.skouter.domain.model.Game
import com.micrantha.skouter.domain.model.Game.Limits
import com.micrantha.skouter.domain.model.Player
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.graphql.GameListQuery
import com.micrantha.skouter.graphql.GameNodeQuery
import com.micrantha.skouter.graphql.GameNodeQuery.Node
import com.micrantha.skouter.graphql.GameNodeQuery.Node2
import kotlinx.datetime.toInstant
import kotlin.time.Duration

class GameDomainMapper {

    fun list(data: GameListQuery.Node) = Game.Listing(
        id = data.id,
        nodeId = data.nodeId,
        name = data.name,
        createdAt = data.created_at.toInstant(),
        expiresAt = data.expires.toInstant(),
        totalPlayers = data.players?.totalCount ?: 0,
        totalThings = data.things?.totalCount ?: 0
    )

    fun map(node: GameNodeQuery.GameNode) = node.onGame!!.let { data ->
        Game(
            id = data.id,
            nodeId = node.nodeId,
            name = data.name,
            createdAt = data.created_at.toInstant(),
            expires = data.expires.toInstant(),
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
                createdAt = data.created_at.toInstant(),
                name = "${data.first_name} ${data.last_name}",
                score = score
            )
        }
    }

    private fun thing(node: Node) = node.thing.let { data ->
        Thing.Listing(
            id = data.id,
            nodeId = data.nodeId,
            createdAt = data.created_at.toInstant(),
            name = data.name,
            guessed = data.guessed ?: false,
            imageUrl = data.imageUrl
        )
    }
}
