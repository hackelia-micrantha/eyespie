package com.micrantha.skouter.ui.player.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.skouter.domain.models.PlayerList

@Composable
fun PlayerListContent(players: PlayerList, dispatch: Dispatch) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(players) {
            PlayerListCard(it)
        }
    }
}
