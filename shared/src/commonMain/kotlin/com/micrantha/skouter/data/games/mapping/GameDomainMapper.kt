package com.micrantha.skouter.data.games.mapping

import com.micrantha.skouter.GameListQuery
import com.micrantha.skouter.domain.models.GameListing
import kotlinx.datetime.LocalDateTime

class GameDomainMapper {
    operator fun invoke(data: GameListQuery.Node) = GameListing(
        id = data._id,
        name = data.name,
        createdAt = LocalDateTime.parse(data._createdAt),
        expiresAt = LocalDateTime.parse(data.expires),
        totalPlayers = data.playersConnection?.totalCount ?: 0,
        totalThings = data.thingsConnection?.totalCount ?: 0
    )
}
