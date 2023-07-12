package com.micrantha.skouter.ui.thing.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.micrantha.bluebell.domain.i18n.longDateTime
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.domain.model.Thing
import com.seiko.imageloader.rememberImagePainter

@Composable
fun ThingListingCard(
    modifier: Modifier = Modifier,
    thing: Thing.Listing,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(Dimensions.content)
        ) {
            GameThingImage(thing.imageUrl)
            Column(
                modifier = Modifier.padding(Dimensions.content)
            ) {
                thing.name?.let {
                    Text(text = it)
                }
                Text(text = longDateTime(thing.createdAt))
            }
        }
    }
}

@Composable
private fun GameThingImage(imageUrl: String) {
    val painter = rememberImagePainter(
        url = imageUrl,
        placeholderPainter = {
            rememberVectorPainter(Icons.Default.Image)
        },
        errorPainter = {
            rememberVectorPainter(Icons.Default.BrokenImage)
        }
    )

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
