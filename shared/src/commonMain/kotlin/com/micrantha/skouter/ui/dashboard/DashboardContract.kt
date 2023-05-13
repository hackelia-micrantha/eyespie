package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.models.GameList
import com.micrantha.skouter.domain.models.PlayerList
import com.micrantha.skouter.domain.models.ThingList

data class DashboardState(
    val games: GameList? = null,
    val players: PlayerList? = null,
    val things: ThingList? = null
)

data class DashboardUiState(
    val status: UiResult<Tabs> = UiResult.Default
) {
    data class Tabs(
        val games: GameList,
        val players: PlayerList,
        val things: ThingList
    )
}

sealed class DashboardAction : Action {
    object Load : DashboardAction()

    object Loaded : DashboardAction()

    object ScanNewThing : DashboardAction()
}

val DashboardState.isValid: Boolean
    get() = games != null && things != null && players != null
