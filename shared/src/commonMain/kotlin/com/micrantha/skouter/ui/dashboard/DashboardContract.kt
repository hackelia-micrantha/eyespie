package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.model.GameList
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.PlayerList
import com.micrantha.skouter.domain.model.ThingList

data class DashboardState(
    val playerID: String? = null,
    val location: Location? = null,
    val games: GameList? = null,
    val players: PlayerList? = null,
    val things: ThingList? = null,
    val status: UiResult<Unit> = UiResult.Default
)

data class DashboardUiState(
    val status: UiResult<Tabs>,
) {
    data class Tabs(
        val games: GameList,
        val players: PlayerList,
        val things: ThingList,
    )
}

sealed class DashboardAction : Action {

    object ScanNewThing : DashboardAction()

    object Load : DashboardAction()

    object LoadError : DashboardAction()

    data class Loaded(val things: ThingList, val games: GameList, val players: PlayerList) :
        DashboardAction()
}
