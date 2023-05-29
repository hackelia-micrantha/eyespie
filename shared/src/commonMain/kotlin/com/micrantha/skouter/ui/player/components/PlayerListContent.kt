package com.micrantha.skouter.ui.player.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.skouter.domain.model.PlayerList
import com.micrantha.skouter.ui.components.S

@Composable
fun PlayerListContent(players: PlayerList, dispatch: Dispatch) {
    when {
        players.isEmpty() -> EmptyContent(stringResource(S.NoPlayersFound))
        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(players) {
                PlayerListCard(player = it)
            }
        }
    }
}
