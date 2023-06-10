package com.micrantha.skouter.data.player.mapping

import com.micrantha.skouter.data.player.model.PlayerResponse
import com.micrantha.skouter.data.system.mapping.LocationDomainMapper
import com.micrantha.skouter.domain.model.Player
import com.micrantha.skouter.domain.model.Player.Name
import com.micrantha.skouter.domain.model.Player.Score
import kotlinx.datetime.toInstant

class PlayerDomainMapper(
    private val locationMapper: LocationDomainMapper
) {

    fun list(data: PlayerResponse) = Player.Listing(
        id = data.id,
        nodeId = data.nodeId ?: data.user_id,
        name = data.nick_name ?: "${data.first_name} ${data.last_name}",
        score = data.total_score,
        createdAt = data.created_at.toInstant()
    )

    fun map(data: PlayerResponse) = Player(
        id = data.id,
        nodeId = data.nodeId ?: data.user_id,
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
        createdAt = data.created_at.toInstant()
    )

}
