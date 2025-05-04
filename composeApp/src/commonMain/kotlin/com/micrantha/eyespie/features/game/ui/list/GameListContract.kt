package com.micrantha.eyespie.features.game.ui.list

import com.micrantha.bluebell.ui.model.UiResult
import com.micrantha.eyespie.domain.entities.GameList

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
