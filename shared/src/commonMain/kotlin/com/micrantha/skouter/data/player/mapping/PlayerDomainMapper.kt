package com.micrantha.skouter.data.player.mapping

import com.micrantha.skouter.data.player.model.PlayerResponse
import com.micrantha.skouter.domain.models.Location
import com.micrantha.skouter.domain.models.Location.Point
import com.micrantha.skouter.domain.models.Player
import com.micrantha.skouter.domain.models.Player.Name
import com.micrantha.skouter.domain.models.Player.Score
import kotlinx.datetime.toInstant

class PlayerDomainMapper {

    fun map(data: List<PlayerResponse>?) = data?.map {
        Player.Listing(
            id = it.id,
            nodeId = it.nodeId ?: it.user_id,
            name = it.nick_name ?: "${it.first_name} ${it.last_name}",
            score = it.total_score,
            createdAt = it.created_at.toInstant()
        )
    } ?: emptyList()

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
        location = data.last_location?.let {
            Location(point = Point(it.first, it.second))
        },
        createdAt = data.created_at.toInstant()
    )

}
