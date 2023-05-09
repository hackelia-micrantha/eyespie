package com.micrantha.skouter.ui.games.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.HorizontalAlignmentLine
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.longDateTime
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.bluebell.ui.components.HorizontalLabeledText
import com.micrantha.bluebell.ui.components.TabPager
import com.micrantha.bluebell.ui.components.status.FailureContent
import com.micrantha.bluebell.ui.components.status.LoadingContent
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.ui.arch.i18n
import com.micrantha.skouter.ui.games.components.GamePlayerCard
import com.micrantha.skouter.ui.games.components.GameThingCard
import kotlin.math.max

@Composable
fun GameDetailsScreen(viewModel: GameDetailsViewModel) {
    val state by viewModel.state().collectAsState()

    GameDetailsContent(state, viewModel::dispatch)
}

@Composable
fun GameDetailsContent(state: GameDetailsUiState, dispatch: Dispatch) {
    when (val status = state.status) {
        is UiResult.Busy -> LoadingContent(status.message)
        is UiResult.Failure -> FailureContent(status.message)
        is UiResult.Ready -> GameDetailsContent(state, status.data, dispatch)
        else -> Unit
    }
}

@Composable
fun GameDetailsContent(state: GameDetailsUiState, game: Game, dispatch: Dispatch) {
    val labelAlignmentLine = remember { HorizontalAlignmentLine(::max) }

    Column(
        modifier = Modifier.fillMaxSize().padding(Dimensions.screen)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(Dimensions.content)) {
                HorizontalLabeledText(
                    label = stringResource(i18n.CreatedAt),
                    text = longDateTime(game.createdAt),
                    labelAlignment = labelAlignmentLine
                )

                HorizontalLabeledText(
                    label = stringResource(i18n.ExpiresAt),
                    text = longDateTime(game.expires),
                    labelAlignment = labelAlignmentLine
                )

                HorizontalLabeledText(
                    label = stringResource(i18n.NextTurn),
                    text = game.turnDuration.toString(),
                    labelAlignment = labelAlignmentLine
                )
            }
        }

        TabPager(
            stringResource(i18n.Things),
            stringResource(i18n.Players),
            stringResource(i18n.Location)
        ) { index, _ ->
            when (index) {
                0 -> GameThingsContent(state, game, dispatch)
                1 -> GamePlayersContent(state, game, dispatch)
                2 -> GameLocationContent(state, dispatch)
            }
        }
    }
}

@Composable
private fun GameThingsContent(
    state: GameDetailsUiState,
    game: Game,
    dispatch: Dispatch
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(game.things) {
            GameThingCard(it, state.image(it.id), dispatch)
        }
    }
}

@Composable
private fun GamePlayersContent(state: GameDetailsUiState, game: Game, dispatch: Dispatch) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(game.players) {
            GamePlayerCard(game, it)
        }
    }
}

@Composable
private fun GameLocationContent(state: GameDetailsUiState, dispatch: Dispatch) {

}
