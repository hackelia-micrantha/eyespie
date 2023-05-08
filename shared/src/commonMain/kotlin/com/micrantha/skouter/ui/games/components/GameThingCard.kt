package com.micrantha.skouter.ui.games.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.longDateTime
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Thing.Image
import com.micrantha.skouter.ui.games.details.GameDetailsAction

@Composable
fun GameThingCard(game: Game, thing: Game.Thing, dispatch: Dispatch) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(Dimensions.content)
        ) {
            if (thing.image != null) {
                GameThingImage(thing.id, thing.image, dispatch)
            } else {
                GameThingPlaceholder()
            }
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
private fun GameThingPlaceholder() {
    Box(
        modifier = Modifier.sizeIn(Dimensions.Icon.placeholder),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.BrokenImage,
            contentDescription = null
        )
    }
}

@Composable
private fun GameThingImage(id: String, image: Image, dispatch: Dispatch) {

    LaunchedEffect(image) {
        if (image.data == null) {
            dispatch(GameDetailsAction.LoadImage(id, image))
        }
    }

    Box(
        modifier = Modifier.sizeIn(Dimensions.Icon.placeholder),
        contentAlignment = Alignment.Center
    ) {
        when (val bitmap = image.data) {
            null -> Icon(
                imageVector = Icons.Default.HourglassTop,
                contentDescription = null
            )
            else ->
                Image(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = bitmap,
                    contentDescription = null
                )
        }
    }
}
