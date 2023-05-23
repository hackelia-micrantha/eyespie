package com.micrantha.skouter.ui.game.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.skouter.domain.models.Game

@Composable
fun GameListContent(games: List<Game.Listing>, dispatch: Dispatch) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(games) { game ->
            GameListCard(game, dispatch)
        }
    }
}
