package com.micrantha.skouter.ui.dashboard.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.ui.components.S
import com.micrantha.skouter.ui.dashboard.DashboardAction.HasMoreNearby
import com.micrantha.skouter.ui.dashboard.DashboardAction.HasMoreThings
import com.micrantha.skouter.ui.dashboard.DashboardAction.ScanNewThing
import com.micrantha.skouter.ui.dashboard.DashboardUiState.Data.ThingsTab
import com.micrantha.skouter.ui.thing.components.ThingListingCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThingTabContent(
    things: ThingsTab,
    dispatch: Dispatch
) {
    when {
        things.isEmpty -> EmptyContent(
            stringResource(S.NoThingsFound),
            icon = Icons.Default.Photo
        ) {
            dispatch(ScanNewThing)
        }
        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            stickyHeader { ListHeader(label = stringResource(S.MyThings)) }
            items(things.owned.data) { thing ->
                ThingListingCard(
                    modifier = Modifier.padding(horizontal = Dimensions.content)
                        .padding(top = Dimensions.content),
                    thing = thing
                )
            }
            if (things.owned.hasMore) {
                item {
                    HasMoreFooter {
                        dispatch(HasMoreThings)
                    }
                }
            }

            stickyHeader { ListHeader(label = stringResource(S.Nearby)) }
            items(things.nearby.data) { thing ->
                ThingListingCard(
                    modifier = Modifier.padding(horizontal = Dimensions.content)
                        .padding(top = Dimensions.content),
                    thing = thing
                )
            }

            if (things.nearby.hasMore) {
                item {
                    HasMoreFooter {
                        dispatch(HasMoreNearby)
                    }
                }
            }
        }
    }
}
