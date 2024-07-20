package com.micrantha.eyespie.ui.dashboard.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.eyespie.domain.model.PlayerList
import com.micrantha.eyespie.ui.component.S
import com.micrantha.eyespie.ui.dashboard.DashboardUiState.Data.TabContent
import com.micrantha.eyespie.ui.player.component.PlayerListCard

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
