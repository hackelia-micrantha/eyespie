package com.micrantha.eyespie.features.game.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.longDateTime
import com.micrantha.bluebell.ui.components.stringResource
import com.micrantha.bluebell.ui.components.LabeledText
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.app.Strings.CreatedAt
import com.micrantha.eyespie.app.Strings.ExpiresAt
import com.micrantha.eyespie.app.Strings.Players
import com.micrantha.eyespie.app.Strings.Things
import com.micrantha.eyespie.domain.entities.Game

@Composable
fun GameListCard(game: Game.Listing, dispatch: Dispatch) = Card(
    modifier = Modifier.padding(Dimensions.screen).fillMaxWidth()
) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(Dimensions.content)
            .clickable(onClick = { dispatch(GameAction.GameClicked(game)) })
    ) {
        Text(
            text = game.name,
            style = MaterialTheme.typography.titleLarge
        )
        LabeledText(
            modifier = Modifier.padding(top = Dimensions.content),
            text = longDateTime(game.createdAt),
            label = stringResource(CreatedAt),
        )

        LabeledText(
            text = longDateTime(game.expiresAt),
            label = stringResource(ExpiresAt),
        )

        Spacer(Modifier.heightIn(Dimensions.screen))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.padding(Dimensions.content),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                LabeledText(
                    modifier = Modifier.padding(Dimensions.content),
                    text = game.totalThings.toString(),
                    label = stringResource(Things),
                )
            }

            Card(
                modifier = Modifier.padding(Dimensions.content),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                LabeledText(
                    modifier = Modifier.padding(Dimensions.content),
                    text = game.totalPlayers.toString(),
                    label = stringResource(Players),
                )
            }
        }
    }
}
