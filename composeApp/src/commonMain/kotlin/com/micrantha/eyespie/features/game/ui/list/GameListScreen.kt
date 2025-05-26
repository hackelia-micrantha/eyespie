package com.micrantha.eyespie.features.game.ui.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.app.Scaffolding
import com.micrantha.bluebell.app.navi.NavAction
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.StateRenderer
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.bluebell.ui.components.status.FailureContent
import com.micrantha.bluebell.ui.components.status.LoadingContent
import com.micrantha.bluebell.ui.model.UiResult.Busy
import com.micrantha.bluebell.ui.model.UiResult.Default
import com.micrantha.bluebell.ui.model.UiResult.Empty
import com.micrantha.bluebell.ui.model.UiResult.Failure
import com.micrantha.bluebell.ui.model.UiResult.Ready
import com.micrantha.bluebell.ui.screen.ScaffoldScreen
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.features.game.ui.component.GameListContent
import com.micrantha.eyespie.features.game.ui.list.GameListAction.Load
import com.micrantha.eyespie.features.game.ui.list.GameListAction.NewGame
import eyespie.composeapp.generated.resources.games
import org.jetbrains.compose.resources.stringResource

class GameListScreen(
    private val context: ScreenContext
) : ScaffoldScreen(), StateRenderer<GameListUiState> {

    @Composable
    override fun Render() {
        val screenModel = rememberScreenModel<GameListScreenModel>()

        val title = stringResource(S.games)

        LaunchedEffect(Unit) {
            screenModel.dispatch(Load)
            screenModel.dispatch(Scaffolding.Title(title))
            screenModel.dispatch(
                Scaffolding.Actions(
                    listOf(
                        NavAction(
                    icon = Icons.Default.Add,
                    action = {
                        it.dispatcher.dispatch(NewGame)
                    }
                ))))
        }

        val state by screenModel.state.collectAsState()

        Render(state, screenModel)
    }

    @Composable
    override fun Render(state: GameListUiState, dispatch: Dispatch) {
        when (val status = state.status) {
            is Busy -> LoadingContent(status.message)
            is Failure -> FailureContent(status.message)
            is Empty -> EmptyContent(status.message)
            is Ready -> GameListContent(status.data, dispatch)
            is Default -> Unit
        }
    }
}
