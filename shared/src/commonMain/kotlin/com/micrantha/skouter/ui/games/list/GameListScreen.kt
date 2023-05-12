package com.micrantha.skouter.ui.games.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.HorizontalAlignmentLine
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.longDateTime
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.domain.model.UiResult.Busy
import com.micrantha.bluebell.domain.model.UiResult.Default
import com.micrantha.bluebell.domain.model.UiResult.Empty
import com.micrantha.bluebell.domain.model.UiResult.Failure
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.bluebell.ui.components.HorizontalLabeledText
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.bluebell.ui.components.status.FailureContent
import com.micrantha.bluebell.ui.components.status.LoadingContent
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.domain.models.GameListing
import com.micrantha.skouter.ui.components.Strings
import kotlin.math.max

class GameListScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<GameListScreenModel>()
        val state by viewModel.state().collectAsState()

        render(state, viewModel::dispatch)
    }

    @Composable
    private fun render(state: GameListUiState, dispatch: Dispatch) {
        when (val status = state.status) {
            is Busy -> LoadingContent(status.message)
            is Failure -> FailureContent(status.message)
            is Empty -> EmptyContent(status.message)
            is Ready -> GameListContent(status.data, dispatch)
            is Default -> Unit
        }
    }
}

@Composable
private fun GameListCard(game: GameListing, onClick: () -> Unit) = Card(
    modifier = Modifier.padding(Dimensions.screen).fillMaxWidth()
) {

    val labelAlignment = remember { HorizontalAlignmentLine(::max) }

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(Dimensions.content)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = game.name,
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalLabeledText(
            modifier = Modifier.padding(top = Dimensions.content),
            text = longDateTime(game.createdAt),
            label = stringResource(Strings.CreatedAt),
            labelAlignment = labelAlignment
        )

        HorizontalLabeledText(
            text = longDateTime(game.expiresAt),
            label = stringResource(Strings.ExpiresAt),
            labelAlignment = labelAlignment
        )

        Spacer(Modifier.heightIn(Dimensions.screen))

        HorizontalLabeledText(
            text = game.totalThings.toString(),
            label = stringResource(Strings.Things),
            labelAlignment = labelAlignment
        )

        HorizontalLabeledText(
            text = game.totalPlayers.toString(),
            label = stringResource(Strings.Players),
            labelAlignment = labelAlignment
        )

    }
}

@Composable
private fun GameListContent(games: List<GameListing>, dispatch: Dispatch) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(games) { game ->
            GameListCard(game) {
                dispatch(GameListActions.GameClicked(game))
            }
        }
    }
}
