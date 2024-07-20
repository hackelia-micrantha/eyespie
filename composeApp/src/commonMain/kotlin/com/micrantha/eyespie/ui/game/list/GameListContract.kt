package com.micrantha.eyespie.ui.game.list

import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.eyespie.domain.model.GameList

data class GameListState(
    val status: UiResult<GameList> = UiResult.Default
)

data class GameListUiState(
    val status: UiResult<GameList>
)

sealed interface GameListAction {
    data object NewGame : GameListAction
    data object Load : GameListAction
    data class Loaded(val data: GameList) : GameListAction
}
