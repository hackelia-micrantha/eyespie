package com.micrantha.skouter.ui.navi

enum class Route(val path: String) {
    Login("login"),
    Games("games"),
    NewGame("games/new"),
    GameDetails("games/details/\$id")
}

enum class NavContext(val initialDestination: Route) {
    Init(Route.Login),
    User(Route.Games),
    CreateGame(Route.NewGame)
}
