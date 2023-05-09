package com.micrantha.skouter.ui.games.list

import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.models.GameListing
import com.micrantha.skouter.domain.repository.GameRepository

data class GameListState(
    val status: UiResult<List<GameListing>> = UiResult.Busy()
)

class GameListEnvironment(
    private val repository: GameRepository,
) : GameRepository by repository

data class GameListUiState(
    val status: UiResult<List<GameListing>>
)

internal fun GameListState.asUiState() = GameListUiState(
    status = status
)
