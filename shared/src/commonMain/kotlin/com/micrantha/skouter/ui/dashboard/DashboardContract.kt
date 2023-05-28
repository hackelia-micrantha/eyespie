package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.model.GameList
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.PlayerList
import com.micrantha.skouter.domain.model.ThingList

const val MaxItemCount = 3

data class DashboardState(
    val playerID: String? = null,
    val location: Location? = null,
    val games: GameList? = null,
    val players: PlayerList? = null,
    val nearbyPlayers: PlayerList? = null,
    val things: ThingList? = null,
    val nearbyThings: ThingList? = null,
    val status: UiResult<Unit> = UiResult.Default
)

data class DashboardUiState(
    val status: UiResult<Data>,
) {
    data class Data(
        val games: GameList,
        val players: PlayerList,
        val things: ThingsTab
    ) {
        data class ThingsTab(
            val owned: TabContent<ThingList>,
            val nearby: TabContent<ThingList>,
            val isEmpty: Boolean,
        )

        data class TabContent<T>(
            val data: T,
            val hasMore: Boolean
        )
    }
}

sealed class DashboardAction : Action {

    object ScanNewThing : DashboardAction()

    object HasMoreThings : DashboardAction()
    object HasMoreNearby : DashboardAction()

    object Load : DashboardAction()

    object LoadError : DashboardAction()

    data class Loaded(val things: ThingList, val games: GameList, val players: PlayerList) :
        DashboardAction()
}
