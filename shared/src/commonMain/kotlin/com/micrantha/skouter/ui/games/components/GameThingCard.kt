package com.micrantha.skouter.ui.games.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.longDateTime
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.ui.games.details.GameDetailsAction

@Composable
fun GameThingCard(thing: Game.Thing, image: UiResult<Painter>, dispatch: Dispatch) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(Dimensions.content)
        ) {
            GameThingImage(thing, image, dispatch)
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
private fun GameThingImage(thing: Game.Thing, image: UiResult<Painter>, dispatch: Dispatch) {

    LaunchedEffect(image) {
        if (thing.image != null && image is UiResult.Default) {
            dispatch(GameDetailsAction.LoadImage(thing.id, thing.image))
        }
    }

    Box(
        modifier = Modifier.size(Dimensions.List.thumbnail),
        contentAlignment = Alignment.Center
    ) {
        when (image) {
            is UiResult.Busy -> Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Default.HourglassTop,
                contentDescription = null
            )
            is UiResult.Ready ->
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = image.data,
                    contentDescription = null
                )
            else -> Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Default.BrokenImage,
                contentDescription = null,
                tint = if (image is UiResult.Failure) {
                    Color.Red
                } else {
                    LocalContentColor.current
                }
            )
        }
    }
}
