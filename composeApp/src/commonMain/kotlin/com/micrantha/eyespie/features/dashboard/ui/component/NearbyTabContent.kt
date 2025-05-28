package com.micrantha.eyespie.features.dashboard.ui.component

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
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.AddFriendClicked
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.GuessThing
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.HasMorePlayers
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.HasMoreThings
import com.micrantha.eyespie.features.dashboard.ui.DashboardUiState.Data.Nearby
import com.micrantha.eyespie.features.players.ui.component.PlayerListCard
import com.micrantha.eyespie.features.things.ui.component.ThingListingCard
import eyespie.composeapp.generated.resources.no_players_found
import eyespie.composeapp.generated.resources.no_things_found
import eyespie.composeapp.generated.resources.players
import eyespie.composeapp.generated.resources.things
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NearbyTabContent(
    tab: Nearby,
    dispatch: Dispatch
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {

        stickyHeader(key = tab.things.data) {
            ListHeader(label = stringResource(S.things))
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
                    message = S.no_things_found,
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
            ListHeader(label = stringResource(S.players))
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
                    message = S.no_players_found,
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
