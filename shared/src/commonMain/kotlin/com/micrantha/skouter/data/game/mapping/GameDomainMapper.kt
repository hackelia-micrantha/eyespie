package com.micrantha.skouter.data.game.mapping

import com.micrantha.bluebell.data.err.fail
import com.micrantha.skouter.GameListQuery
import com.micrantha.skouter.GameNodeQuery
import com.micrantha.skouter.GameNodeQuery.Edge1
import com.micrantha.skouter.GameNodeQuery.Edge2
import com.micrantha.skouter.data.thing.model.ImageJson
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Game.Limits
import com.micrantha.skouter.domain.models.Image
import com.micrantha.skouter.domain.models.Player
import com.micrantha.skouter.domain.models.PlayerList
import com.micrantha.skouter.domain.models.Thing
import kotlinx.datetime.Clock.System
import kotlinx.datetime.toInstant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.time.Duration

class GameDomainMapper {
    operator fun invoke(data: GameListQuery.Node) = Game.Listing(
        id = data.id,
        nodeId = data.nodeId,
        name = data.name,
        createdAt = data.created_at.toInstant(),
        expiresAt = data.expires.toInstant(),
        totalPlayers = data.players?.totalCount ?: 0,
        totalThings = data.things?.totalCount ?: 0
    )

    operator fun invoke(nodeId: String, data: GameNodeQuery.OnGame) = Game(
        id = data.id,
        nodeId = nodeId,
        name = data.name,
        createdAt = data.created_at.toInstant(),
        expires = data.expires.toInstant(),
        turnDuration = Duration.parse(data.turn_duration),
        players = mapPlayers(data.players?.edges?.filterNotNull()) ?: emptyList(),
        things = mapThings(data.things?.edges?.filterNotNull()) ?: emptyList(),
        limits = Limits(
            player = IntRange(data.min_players ?: 1, data.max_players ?: 10),
            thing = IntRange(data.min_things ?: 1, data.max_things ?: 10)
        )
    )

    private fun mapPlayers(data: List<Edge2>?): PlayerList? =
        data?.map { it.node }?.map { node ->
            val score = node.score ?: 0
            node.player.let {
                Player.Listing(
                    id = it.id,
                    nodeId = it.nodeId,
                    createdAt = it.created_at.toInstant(),
                    name = "${it.first_name} ${it.last_name}",
                    score = score
                )
            }
        }

    private fun mapThings(data: List<GameNodeQuery.Edge>?) =
        data?.map { it.node.thing }?.map { thing ->
            Thing.Listing(
                id = thing.id,
                nodeId = thing.nodeId,
                createdAt = thing.created_at.toInstant(),
                name = thing.name,
                guessed = thing.guessed ?: false,
                image = thing.image?.let { json ->
                    mapImage(Json.decodeFromString(json.toString()))
                } ?: fail("no image for thing(${thing.id})")
            )
        }

    private fun mapGuesses(data: List<Edge1>?) = data?.mapNotNull { it.node }?.map { guess ->
        Thing.Guess(
            at = guess.created_at?.toInstant() ?: System.now(),
            by = guess.created_by?.let { by ->
                Player.Ref(by.nodeId, by.name)
            } ?: fail("invalid player ref"),
            correct = guess.correct ?: false
        )
    }

    private fun mapImage(data: ImageJson) = Image(
        fileId = data.fileId,
        bucketId = data.bucketId,
        playerId = data.playerId
    )
}
