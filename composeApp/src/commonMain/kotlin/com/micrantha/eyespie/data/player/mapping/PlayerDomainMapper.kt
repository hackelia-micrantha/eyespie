package com.micrantha.eyespie.data.player.mapping

import com.micrantha.eyespie.data.player.model.PlayerResponse
import com.micrantha.eyespie.data.system.mapping.LocationDomainMapper
import com.micrantha.eyespie.domain.model.Player
import com.micrantha.eyespie.domain.model.Player.Name
import com.micrantha.eyespie.domain.model.Player.Score
import kotlinx.datetime.Instant

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
