package com.micrantha.skouter.ui.thing.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.i18n.longDateTime
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.domain.model.Thing
import com.seiko.imageloader.rememberAsyncImagePainter

@Composable
fun ThingListingCard(modifier: Modifier = Modifier, thing: Thing.Listing) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(Dimensions.content)
        ) {
            GameThingImage(thing.imageUrl)
            Column(
                modifier = Modifier.padding(Dimensions.content)
            ) {
                Text(
                    text = thing.name
                )
                Text(
                    text = longDateTime(thing.createdAt)
                )
            }
        }
    }
}

@Composable
private fun GameThingImage(imageUrl: String) {
    val painter = rememberAsyncImagePainter(imageUrl)

    Box(
        modifier = Modifier.size(Dimensions.List.thumbnail),
        contentAlignment = Alignment.Center
    ) {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = null
        )
    }
}
