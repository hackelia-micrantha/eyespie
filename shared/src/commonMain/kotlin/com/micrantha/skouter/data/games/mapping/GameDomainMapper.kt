package com.micrantha.skouter.data.games.mapping

import com.micrantha.skouter.GameListQuery
import com.micrantha.skouter.domain.models.GameListing
import kotlinx.datetime.TimeZone
import kotlinx.datetime.TimeZone.Companion
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class GameDomainMapper {
    operator fun invoke(data: GameListQuery.Node) = GameListing(
        id = data.id,
        name = data.name,
        createdAt = data.created_at.toInstant().toLocalDateTime(Companion.UTC),
        expiresAt = data.expires.toInstant().toLocalDateTime(TimeZone.UTC),
        totalPlayers = data.gamePlayersCollection?.totalCount ?: 0,
        totalThings = data.gameThingsCollection?.totalCount ?: 0
    )
}
