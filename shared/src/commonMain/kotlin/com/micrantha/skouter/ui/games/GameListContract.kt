package com.micrantha.skouter.ui.games

import com.micrantha.bluebell.domain.status.ModelStatus
import com.micrantha.skouter.domain.models.GameListing
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.domain.repository.GameRepository

data class GameListState(
    val games: List<GameListing>? = null,
    val status: ModelStatus = ModelStatus.Busy()
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
    val status: ModelStatus
)

internal fun GameListState.asUiState() = GameListUiState(
    games = games ?: emptyList(),
    status = status
)
