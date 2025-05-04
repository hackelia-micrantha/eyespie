package com.micrantha.eyespie.features.dashboard.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.stringResource
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.domain.entities.PlayerList
import com.micrantha.eyespie.features.dashboard.ui.DashboardUiState.Data.TabContent
import com.micrantha.eyespie.features.players.ui.component.PlayerListCard

@Composable
fun FriendsTabContent(tab: TabContent<PlayerList>, dispatch: Dispatch) {
    when {
        tab.data.isEmpty() -> EmptyContent(stringResource(S.NoPlayersFound))
        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tab.data, key = { it.id }) {
                PlayerListCard(player = it)
            }
        }
    }
}
