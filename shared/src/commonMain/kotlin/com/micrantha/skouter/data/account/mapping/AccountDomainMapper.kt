package com.micrantha.skouter.data.account.mapping

import com.micrantha.skouter.data.account.model.AccountResponse
import com.micrantha.skouter.domain.models.Location
import com.micrantha.skouter.domain.models.Location.Point
import com.micrantha.skouter.domain.models.Player
import kotlinx.datetime.toInstant

class AccountDomainMapper {

    operator fun invoke(data: AccountResponse) = Player(
        id = data.id,
        nodeId = data.nodeId ?: data.user_id,
        name = Player.Name(
            first = data.first_name,
            last = data.last_name,
            nick = data.nick_name
        ),
        email = data.email ?: "",
        score = Player.Score(
            total = data.total_score
        ),
        location = data.last_location?.let {
            Location(point = Point(it.first, it.second))
        },
        createdAt = data.created_at.toInstant()
    )
}
