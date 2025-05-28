package com.micrantha.eyespie.features.game.ui.detail

import com.micrantha.bluebell.ui.model.UiResult
import com.micrantha.eyespie.domain.entities.Game

data class GameDetailScreenArg(
    val id: String,
    val title: String
)

data class GameDetailsState(
    val game: Game? = null,
    val status: UiResult<Unit> = UiResult.Default,
)

data class GameDetailsUiState(
    val status: UiResult<Game>
)

sealed interface GameDetailsAction {
    data class Load(val id: String) : GameDetailsAction
    data class Loaded(val game: Game) : GameDetailsAction
}

