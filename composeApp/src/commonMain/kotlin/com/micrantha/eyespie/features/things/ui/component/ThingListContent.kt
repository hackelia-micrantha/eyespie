package com.micrantha.eyespie.features.things.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.domain.entities.ThingList
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.ScanNewThing
import eyespie.composeapp.generated.resources.no_things_found

@Composable
fun ThingListContent(
    things: ThingList,
    dispatch: Dispatch
) {
    when {
        things.isEmpty() -> EmptyContent(
            S.no_things_found,
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
