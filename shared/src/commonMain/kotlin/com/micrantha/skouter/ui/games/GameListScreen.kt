package com.micrantha.skouter.ui.games

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.micrantha.bluebell.domain.status.ModelStatus
import com.micrantha.bluebell.ui.status.FailureContent
import com.micrantha.bluebell.ui.status.LoadingContent

@Composable
fun GameListScreen(viewModel: GameListViewModel) {
    val state by viewModel.state().collectAsState()

    when (val status = state.status) {
        is ModelStatus.Busy -> LoadingContent(status.message)
        is ModelStatus.Failure -> FailureContent(status.error)
        else -> GameListContent(state)
    }

}

@Composable
private fun GameListContent(state: GameListUiState) {
    LazyColumn {
        items(state.games) { game ->
            Card {
                Text(game.name)

                Text(game.totalThings.toString())

                Text(game.totalPlayers.toString())

                Text(game.expiresAt.toString())

                Text(game.createdAt.toString())
            }
        }
    }
}
