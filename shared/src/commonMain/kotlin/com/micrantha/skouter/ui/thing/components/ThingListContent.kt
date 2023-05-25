package com.micrantha.skouter.ui.thing.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.skouter.domain.models.ThingList
import com.micrantha.skouter.ui.components.S
import com.micrantha.skouter.ui.dashboard.DashboardAction.ScanNewThing

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
        else -> LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(things) { thing ->
                ThingListingCard(thing, dispatch)
            }
        }
    }
}
