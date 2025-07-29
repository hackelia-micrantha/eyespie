package com.micrantha.eyespie.features.game.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.app.Scaffolding
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.LabeledText
import com.micrantha.bluebell.ui.components.StateRenderer
import com.micrantha.bluebell.ui.components.TabPager
import com.micrantha.bluebell.ui.components.longDateTime
import com.micrantha.bluebell.ui.components.status.FailureContent
import com.micrantha.bluebell.ui.components.status.LoadingContent
import com.micrantha.bluebell.ui.model.UiResult
import com.micrantha.bluebell.ui.screen.ScaffoldScreen
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.domain.entities.Game
import com.micrantha.eyespie.features.game.ui.detail.GameDetailsAction.Load
import com.micrantha.eyespie.features.players.ui.component.PlayerListContent
import com.micrantha.eyespie.features.things.ui.component.ThingListContent
import eyespie.composeapp.generated.resources.created_at
import eyespie.composeapp.generated.resources.expires_at
import eyespie.composeapp.generated.resources.location
import eyespie.composeapp.generated.resources.next_turn
import eyespie.composeapp.generated.resources.players
import eyespie.composeapp.generated.resources.things
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

data class GameDetailsScreen(private val context: ScreenContext, private val arg: GameDetailScreenArg) : ScaffoldScreen(context),
    StateRenderer<GameDetailsUiState> {

    @Composable
    override fun Render() {
        val screenModel: GameDetailsScreenModel = rememberScreenModel()

        LaunchedEffect(Unit) {
            screenModel.dispatch(Load(arg.id))
            screenModel.dispatch(Scaffolding.Title(arg.title))
        }

        val state by screenModel.state.collectAsState()

        Render(state, screenModel)
    }

    @Composable
    override fun Render(state: GameDetailsUiState, dispatch: Dispatch) {
        when (val status = state.status) {
            is UiResult.Busy -> LoadingContent(status.message)
            is UiResult.Failure -> FailureContent(status.message)
            is UiResult.Ready -> GameDetailsContent(status.data, dispatch)
            else -> Unit
        }
    }

    @OptIn(ExperimentalTime::class)
    @Composable
    private fun GameDetailsContent(game: Game, dispatch: Dispatch) {

        Column(
            modifier = Modifier.fillMaxSize().padding(Dimensions.screen)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(Dimensions.content)) {
                    LabeledText(
                        label = stringResource(S.created_at),
                        text = longDateTime(game.createdAt),
                    )

                    LabeledText(
                        label = stringResource(S.expires_at),
                        text = longDateTime(game.expires),
                    )

                    LabeledText(
                        label = stringResource(S.next_turn),
                        text = game.turnDuration.toString(),
                    )
                }
            }

            TabPager(
                stringResource(S.things),
                stringResource(S.players),
                stringResource(S.location)
            ) { index, _ ->
                when (index) {
                    0 -> ThingListContent(game.things, dispatch)
                    1 -> PlayerListContent(game.players, dispatch)
                    2 -> GameLocationContent()
                }
            }
        }
    }

    @Composable
    private fun GameLocationContent() {

    }
}
