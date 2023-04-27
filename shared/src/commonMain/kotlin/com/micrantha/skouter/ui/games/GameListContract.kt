package com.micrantha.skouter.ui.games

import com.micrantha.bluebell.domain.model.ResultStatus
import com.micrantha.skouter.domain.models.GameListing
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.domain.repository.GameRepository

data class GameListState(
    val games: List<GameListing>? = null,
    val status: ResultStatus = ResultStatus.Busy()
)

class GameListEnvironment(
    private val repository: GameRepository,
    private val accountRepository: AccountRepository
) : GameRepository by repository {
    suspend fun test() {
        accountRepository.loginAnonymous()
    }
}

data class GameListUiState(
    val games: List<GameListing>,
    val status: ResultStatus
)

internal fun GameListState.asUiState() = GameListUiState(
    games = games ?: emptyList(),
    status = status
)
