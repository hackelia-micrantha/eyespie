package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.PlayerList
import com.micrantha.skouter.domain.model.ThingList

const val MaxItemCount = 3

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

sealed class DashboardAction : Action {

    object ScanNewThing : DashboardAction()

    object HasMoreThings : DashboardAction()

    object HasMorePlayers : DashboardAction()

    object Load : DashboardAction()

    object LoadError : DashboardAction()

    data class Loaded(val things: ThingList, val friends: PlayerList, val players: PlayerList) :
        DashboardAction()

    object AddFriendClicked : DashboardAction()
}
