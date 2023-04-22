package com.micrantha.skouter.ui.navi

import com.micrantha.bluebell.ui.navi.Route
import com.micrantha.bluebell.ui.navi.routes
import com.micrantha.skouter.ui.games.GameListScreen
import com.micrantha.skouter.ui.games.GameListViewModel
import com.micrantha.skouter.ui.navi.AppRoutes.Games

enum class AppRoutes(override val path: String, override val isDefault: Boolean = false) : Route {
    Games("games", true),
    NewGame("games/new")
}

fun routes() = routes {
    Games to { viewModel: GameListViewModel ->
        GameListScreen(viewModel)
    }
}.build()
