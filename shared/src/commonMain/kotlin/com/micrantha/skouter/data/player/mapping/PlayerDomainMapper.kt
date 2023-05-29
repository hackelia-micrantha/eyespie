package com.micrantha.skouter.data.player.mapping

import com.micrantha.skouter.data.player.model.PlayerResponse
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.Location.Point
import com.micrantha.skouter.domain.model.Player
import com.micrantha.skouter.domain.model.Player.Name
import com.micrantha.skouter.domain.model.Player.Score
import kotlinx.datetime.toInstant
import kotlinx.serialization.json.jsonPrimitive

class PlayerDomainMapper {

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
            point(json.jsonPrimitive.content)?.let {
                Location(point = it)
            }
        },
        createdAt = data.created_at.toInstant()
    )

    private fun point(data: String): Point? {
        var start = data.indexOf('(')
        if (start == -1) return null
        var end = data.indexOf(',', start)
        if (end == -1 || start >= end) return null
        val latitude = data.substring(start + 1, end).toDouble()

        start = end
        end = data.indexOf(')', start)
        if (end == -1 || start >= end) return null
        val longitude = data.substring(start + 1, end).toDouble()
        
        return Point(latitude = latitude, longitude = longitude)
    }
}
