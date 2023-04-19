package com.micrantha.skouter.presentation.screens.games

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.micrantha.bluebell.ui.arch.Dispatch

@Composable
fun GameListScreen(viewModel: GameListViewModel) {
    val state by viewModel.state().collectAsState()

    GameListContent(state)
}

@Composable
private fun GameListContent(state: GameListUiState) {
    LazyColumn {
        items(state.games) { game ->
            Card {
                Text(game.name)

                Text(game.numberOfThings.toString())

                Text(game.numberOfPlayers.toString())

                Text(game.expiresAt)

                Text(game.createdAt)
            }
        }
    }
}
