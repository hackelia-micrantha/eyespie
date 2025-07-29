package com.micrantha.eyespie.features.dashboard.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.features.dashboard.ui.DashboardUiState.Data.TabContent
import com.micrantha.eyespie.features.players.domain.entities.PlayerList
import com.micrantha.eyespie.features.players.ui.component.PlayerListCard
import eyespie.composeapp.generated.resources.no_players_found

@Composable
fun FriendsTabContent(tab: TabContent<PlayerList>, dispatch: Dispatch) {
    when {
        tab.data.isEmpty() -> EmptyContent(S.no_players_found)
        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tab.data, key = { it.id }) {
                PlayerListCard(player = it)
            }
        }
    }
}
