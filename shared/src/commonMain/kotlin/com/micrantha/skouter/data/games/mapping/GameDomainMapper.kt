package com.micrantha.skouter.data.games.mapping

import com.micrantha.skouter.GameListQuery
import com.micrantha.skouter.GameNodeQuery
import com.micrantha.skouter.data.things.model.ImageJson
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Game.Limits
import com.micrantha.skouter.domain.models.Game.Player
import com.micrantha.skouter.domain.models.Game.Thing
import com.micrantha.skouter.domain.models.GameListing
import com.micrantha.skouter.domain.models.Thing.Image
import kotlinx.datetime.toInstant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.time.Duration

class GameDomainMapper {
    operator fun invoke(data: GameListQuery.Node) = GameListing(
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

    private fun mapPlayers(data: List<GameNodeQuery.Edge1>?): List<Player>? =
        data?.map { it.node }?.map { node ->
            val score = node.score ?: 0
            node.player.let {
                Player(
                    id = it.id,
                    nodeId = it.nodeId,
                    createdAt = it.created_at.toInstant(),
                    name = "${it.first_name} ${it.last_name}",
                    score = score
                )
            }
        }

    private fun mapThings(data: List<GameNodeQuery.Edge>?) =
        data?.map { it.node.thing }?.map {
            Thing(
                id = it.id,
                nodeId = it.nodeId,
                createdAt = it.created_at.toInstant(),
                name = it.name,
                guessed = it.guessed ?: false,
                image = it.image?.let {
                    mapImage(Json.decodeFromString(it.toString()))
                }
            )
        }

    private fun mapImage(data: ImageJson) = Image(
        fileId = data.fileId,
        bucketId = data.bucketId,
        playerId = data.playerId
    )
}
