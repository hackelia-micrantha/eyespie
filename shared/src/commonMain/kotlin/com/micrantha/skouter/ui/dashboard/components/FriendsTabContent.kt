package com.micrantha.skouter.ui.dashboard.components

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
import com.micrantha.skouter.ui.dashboard.DashboardUiState.Data.TabContent
import com.micrantha.skouter.ui.player.components.PlayerListCard

@Composable
fun FriendsTabContent(tab: TabContent<PlayerList>, dispatch: Dispatch) {
    when {
        tab.data.isEmpty() -> EmptyContent(stringResource(S.NoPlayersFound))
        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tab.data) {
                PlayerListCard(it)
            }
        }
    }
}
