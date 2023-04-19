package com.micrantha.skouter.presentation.screens.games

import com.micrantha.bluebell.ui.arch.Action
import com.micrantha.skouter.domain.models.Game

data class GameListState(
    val games: List<Game>? = null
)

data class GameListUiState(
    val games: List<ListItem>
) {
    data class ListItem(
        val name: String,
        val numberOfThings: Int,
        val numberOfPlayers: Int,
        val expiresAt: String,
        val createdAt: String
    )
}

sealed class GameListActions : Action {
    object NewGame : GameListActions()
}

internal fun mapper(state: GameListState) = GameListUiState(
    state.games?.map(::mapGame) ?: emptyList()
)

internal fun mapGame(game: Game) =
    GameListUiState.ListItem(
        game.name,
        game.things.size,
        game.players.size,
        game.expires.toString(),
        game.createdAt.toString()
    )
