package com.micrantha.skouter.ui.navi

import com.micrantha.bluebell.ui.navi.Route
import com.micrantha.bluebell.ui.navi.RouteContext
import com.micrantha.bluebell.ui.navi.routes
import com.micrantha.skouter.ui.games.create.GameCreateScreen
import com.micrantha.skouter.ui.games.create.GameCreateViewModel
import com.micrantha.skouter.ui.games.details.GameDetailsScreen
import com.micrantha.skouter.ui.games.details.GameDetailsViewModel
import com.micrantha.skouter.ui.games.list.GameListScreen
import com.micrantha.skouter.ui.games.list.GameListViewModel
import com.micrantha.skouter.ui.login.LoginScreen
import com.micrantha.skouter.ui.login.LoginViewModel
import com.micrantha.skouter.ui.navi.Routes.GameDetails
import com.micrantha.skouter.ui.navi.Routes.Games
import com.micrantha.skouter.ui.navi.Routes.Login
import com.micrantha.skouter.ui.navi.Routes.NewGame

enum class Routes(override val path: String) : Route {
    Login("login"),
    Games("games"),
    NewGame("games/new"),
    GameDetails("games/details/\$id")
}

enum class NavContext(override val initialDestination: Route) : RouteContext {
    Init(Login),
    User(Games),
    CreateGame(NewGame)
}

fun routes() = routes {
    initialContext = NavContext.Init

    Login to { viewModel: LoginViewModel ->
        LoginScreen(viewModel)
    }
    Games to { viewModel: GameListViewModel ->
        GameListScreen(viewModel)
    }
    GameDetails to { viewModel: GameDetailsViewModel ->
        GameDetailsScreen(viewModel)
    }
    NewGame to { viewModel: GameCreateViewModel ->
        GameCreateScreen(viewModel)
    }
}.build()
