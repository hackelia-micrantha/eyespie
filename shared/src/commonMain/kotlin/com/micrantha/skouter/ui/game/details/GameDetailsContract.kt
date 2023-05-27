package com.micrantha.skouter.ui.game.details

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.model.Game

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

sealed class GameDetailsAction : Action {
    data class Load(val arg: GameDetailScreenArg) : GameDetailsAction()
    data class Loaded(val game: Game) : GameDetailsAction()
}

