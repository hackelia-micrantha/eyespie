package com.micrantha.skouter.data.games.mapping

import com.micrantha.skouter.GameListQuery
import com.micrantha.skouter.domain.models.GameListing
import kotlinx.datetime.LocalDateTime

class GameDomainMapper {
    operator fun invoke(data: GameListQuery.Node) = GameListing(
        id = data.id,
        name = data.name,
        createdAt = LocalDateTime.parse(data.created_at),
        expiresAt = LocalDateTime.parse(data.expires),
        totalPlayers = 0,
        totalThings = 0
    )
}
