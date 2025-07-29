package com.micrantha.eyespie.features.players.data.mapping

import com.micrantha.eyespie.core.data.system.mapping.LocationDomainMapper
import com.micrantha.eyespie.features.players.data.model.PlayerResponse
import com.micrantha.eyespie.features.players.domain.entities.Player
import com.micrantha.eyespie.features.players.domain.entities.Player.Name
import com.micrantha.eyespie.features.players.domain.entities.Player.Score
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class PlayerDomainMapper(
    private val locationMapper: LocationDomainMapper
) {

    fun list(data: PlayerResponse) = Player.Listing(
        id = data.id,
        nodeId = data.nodeId ?: data.user_id,
        name = data.nick_name ?: "${data.first_name} ${data.last_name}",
        score = data.total_score,
        createdAt = Instant.parse(data.created_at)
    )

    fun map(data: PlayerResponse) = Player(
        id = data.id,
        name = Name(
            first = data.first_name,
            last = data.last_name,
            nick = data.nick_name
        ),
        email = data.email ?: "",
        score = Score(
            total = data.total_score
        ),
        location = data.last_location?.let { json ->
            locationMapper.map(json)
        },
        createdAt = Instant.parse(data.created_at)
    )

}
