package com.micrantha.skouter.ui.game.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.model.UiResult.Busy
import com.micrantha.bluebell.domain.model.UiResult.Default
import com.micrantha.bluebell.domain.model.UiResult.Empty
import com.micrantha.bluebell.domain.model.UiResult.Failure
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.bluebell.ui.components.StateRenderer
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.bluebell.ui.components.status.FailureContent
import com.micrantha.bluebell.ui.components.status.LoadingContent
import com.micrantha.bluebell.ui.scaffold.ScaffoldScreen
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.skouter.ui.component.Strings.Games
import com.micrantha.skouter.ui.game.component.GameListContent
import com.micrantha.skouter.ui.game.list.GameListAction.Load
import com.micrantha.skouter.ui.game.list.GameListAction.NewGame
import com.micrantha.skouter.ui.navi.NavAction

class GameListScreen(
    private val context: ScreenContext
) : ScaffoldScreen(), StateRenderer<GameListUiState> {
    @Composable
    override fun Render() {
        val viewModel = rememberScreenModel<GameListScreenModel>()

        LaunchedEffect(Unit) {
            viewModel.dispatch(Load)
        }

        val state by viewModel.state.collectAsState()

        Render(state, viewModel::dispatch)
    }

    override fun actions() = listOf(NavAction(
        icon = Icons.Default.Add,
        action = {
            it.dispatcher.dispatch(NewGame)
        }
    ))

    override fun title() = context.i18n.string(Games)

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
