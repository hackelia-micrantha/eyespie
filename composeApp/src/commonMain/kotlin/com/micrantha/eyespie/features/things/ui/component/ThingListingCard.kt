package com.micrantha.eyespie.features.things.ui.component

import androidx.compose.foundation.clickable
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
import com.micrantha.bluebell.ui.components.longDateTime
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.app.AppConfig
import com.micrantha.eyespie.domain.entities.ImagePath
import com.micrantha.eyespie.domain.entities.Thing
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

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
private fun GameThingImage(imageUrl: ImagePath) {
    val painter = asyncPainterResource(
        key = imageUrl,
        data = "${AppConfig.SUPABASE_URL}/storage/v1/${imageUrl}"
    )

    Box(
        modifier = Modifier.size(Dimensions.List.thumbnail),
        contentAlignment = Alignment.Center
    ) {

        KamelImage(
            modifier = Modifier.fillMaxSize(),
            resource = painter,
            contentDescription = null
        )
    }
}
