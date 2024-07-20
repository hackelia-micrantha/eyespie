package com.micrantha.eyespie.ui.thing.component

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
import com.micrantha.eyespie.domain.model.ThingList
import com.micrantha.eyespie.ui.component.S
import com.micrantha.eyespie.ui.dashboard.DashboardAction.ScanNewThing

@Composable
fun ThingListContent(
    things: ThingList,
    dispatch: Dispatch
) {
    when {
        things.isEmpty() -> EmptyContent(
            stringResource(S.NoThingsFound),
            icon = Icons.Default.Photo
        ) {
            dispatch(ScanNewThing)
        }

        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(things, key = { it.id }) { thing ->
                ThingListingCard(
                    modifier = Modifier.padding(Dimensions.content),
                    thing = thing
                )
            }
        }
    }
}
