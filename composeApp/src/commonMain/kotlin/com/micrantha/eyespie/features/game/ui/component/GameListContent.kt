package com.micrantha.eyespie.features.game.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.domain.entities.Game
import com.micrantha.eyespie.features.game.ui.list.GameListAction.NewGame
import eyespie.composeapp.generated.resources.no_games_found

@Composable
fun GameListContent(games: List<Game.Listing>, dispatch: Dispatch) {
    when {
        games.isEmpty() -> EmptyContent(
            message = S.no_games_found,
            icon = Icons.Default.SmartToy
        ) {
            dispatch(NewGame)
        }

        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(games, key = { it.id }) { game ->
                GameListCard(game, dispatch)
            }
        }
    }
}
