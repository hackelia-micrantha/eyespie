package com.micrantha.skouter.ui.games.list

import com.micrantha.bluebell.domain.model.ResultStatus
import com.micrantha.skouter.domain.models.GameListing
import com.micrantha.skouter.domain.repository.GameRepository

data class GameListState(
    val status: ResultStatus<List<GameListing>> = ResultStatus.Busy()
)

class GameListEnvironment(
    private val repository: GameRepository,
) : GameRepository by repository

data class GameListUiState(
    val status: ResultStatus<List<GameListing>>
)

internal fun GameListState.asUiState() = GameListUiState(
    status = status
)
