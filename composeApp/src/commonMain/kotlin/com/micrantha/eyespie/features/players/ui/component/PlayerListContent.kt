package com.micrantha.eyespie.features.players.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.domain.entities.PlayerList
import eyespie.composeapp.generated.resources.no_players_found

@Composable
fun PlayerListContent(players: PlayerList, dispatch: Dispatch) {
    when {
        players.isEmpty() -> EmptyContent(S.no_players_found)
        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(players, key = { it.id }) {
                PlayerListCard(player = it)
            }
        }
    }
}
