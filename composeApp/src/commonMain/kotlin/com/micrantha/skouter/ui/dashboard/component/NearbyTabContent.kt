package com.micrantha.skouter.ui.dashboard.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Photo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.ui.component.S
import com.micrantha.skouter.ui.dashboard.DashboardAction.AddFriendClicked
import com.micrantha.skouter.ui.dashboard.DashboardAction.GuessThing
import com.micrantha.skouter.ui.dashboard.DashboardAction.HasMorePlayers
import com.micrantha.skouter.ui.dashboard.DashboardAction.HasMoreThings
import com.micrantha.skouter.ui.dashboard.DashboardUiState.Data.Nearby
import com.micrantha.skouter.ui.player.component.PlayerListCard
import com.micrantha.skouter.ui.thing.component.ThingListingCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NearbyTabContent(
    tab: Nearby,
    dispatch: Dispatch
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {

        stickyHeader(key = tab.things.data) {
            ListHeader(label = stringResource(S.Things))
        }
        itemsIndexed(
            items = tab.things.data,
            key = { _, item -> item.id }
        ) { index, thing ->
            ThingListingCard(
                modifier = Modifier.padding(horizontal = Dimensions.content).then(
                    if (index == 0)
                        Modifier.padding(vertical = Dimensions.content)
                    else
                        Modifier.padding(bottom = Dimensions.content)
                ),
                thing = thing
            ) {
                dispatch(GuessThing(thing))
            }
        }
        when {
            tab.things.data.isEmpty() -> item {
                EmptyContent(
                    message = stringResource(S.NoThingsFound),
                    size = Dimensions.List.thumbnail,
                    icon = Icons.Default.Photo
                )
            }

            tab.things.hasMore -> item {
                HasMoreFooter {
                    dispatch(HasMoreThings)
                }
            }
        }
        stickyHeader(key = tab.players.data) {
            ListHeader(label = stringResource(S.Players))
        }
        itemsIndexed(tab.players.data, key = { _, item -> item.id }) { index, player ->
            PlayerListCard(
                modifier = Modifier.padding(horizontal = Dimensions.content).then(
                    if (index == 0) {
                        Modifier.padding(vertical = Dimensions.content)
                    } else {
                        Modifier.padding(bottom = Dimensions.content)
                    }
                ).padding(bottom = Dimensions.content),
                player = player
            )
        }
        when {
            tab.players.data.isEmpty() -> item {
                EmptyContent(
                    message = stringResource(S.NoPlayersFound),
                    size = Dimensions.List.thumbnail,
                    icon = Icons.Default.People
                ) {
                    dispatch(AddFriendClicked)
                }
            }

            tab.players.hasMore -> item {
                HasMoreFooter {
                    dispatch(HasMorePlayers)
                }
            }
        }
    }
}
