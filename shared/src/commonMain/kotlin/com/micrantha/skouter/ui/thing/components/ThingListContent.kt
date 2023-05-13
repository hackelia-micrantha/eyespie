package com.micrantha.skouter.ui.thing.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.skouter.domain.models.ThingList

@Composable
fun ThingListContent(
    things: ThingList,
    dispatch: Dispatch
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(things) { thing ->
            ThingListingCard(thing, dispatch)
        }
    }
}
