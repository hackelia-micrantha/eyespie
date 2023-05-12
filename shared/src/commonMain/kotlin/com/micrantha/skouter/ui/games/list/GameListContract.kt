package com.micrantha.skouter.ui.games.list

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.models.GameListing
import com.micrantha.skouter.ui.games.details.GameDetailScreenArg

data class GameListState(
    val status: UiResult<List<GameListing>> = UiResult.Default
)

data class GameListUiState(
    val status: UiResult<List<GameListing>>
)

sealed class GameListActions : Action {
    object NewGame : GameListActions()
    object Load : GameListActions()
    data class Loaded(val data: List<GameListing>) : GameListActions()
    data class Error(val error: Throwable) : GameListActions()

    data class GameClicked(
        val game: GameListing,
        val arg: GameDetailScreenArg = GameDetailScreenArg(game.nodeId, game.name)
    ) : GameListActions()
}
