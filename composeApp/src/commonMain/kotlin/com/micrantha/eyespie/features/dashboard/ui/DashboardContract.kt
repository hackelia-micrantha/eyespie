package com.micrantha.eyespie.features.dashboard.ui

import com.micrantha.bluebell.ui.model.UiResult
import com.micrantha.eyespie.domain.entities.Location
import com.micrantha.eyespie.domain.entities.Thing
import com.micrantha.eyespie.domain.entities.ThingList
import com.micrantha.eyespie.features.players.domain.entities.PlayerList

data class DashboardState(
    val playerID: String? = null,
    val location: Location? = null,
    val friends: PlayerList? = null,
    val things: ThingList? = null,
    val players: PlayerList? = null,
    val status: UiResult<Unit> = UiResult.Default
)

data class DashboardUiState(
    val status: UiResult<Data>,
) {
    data class Data(
        val nearby: Nearby,
        val friends: TabContent<PlayerList>
    ) {
        data class TabContent<T>(
            val data: T,
            val hasMore: Boolean
        )

        data class Nearby(
            val players: TabContent<PlayerList>,
            val things: TabContent<ThingList>
        )
    }
}

sealed interface DashboardAction {

    data object ScanNewThing : DashboardAction

    data object HasMoreThings : DashboardAction

    data object HasMorePlayers : DashboardAction

    data object Load : DashboardAction

    data object LoadError : DashboardAction

    data class Loaded(val nearbyThings: ThingList, val nearbyPlayers: PlayerList, val friends: PlayerList) :
        DashboardAction

    data object AddFriendClicked : DashboardAction

    data class GuessThing(val thing: Thing.Listing) : DashboardAction
}
