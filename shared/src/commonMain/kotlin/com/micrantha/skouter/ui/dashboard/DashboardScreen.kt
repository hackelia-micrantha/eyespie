package com.micrantha.skouter.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.domain.model.UiResult.Failure
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.bluebell.ui.components.TabPager
import com.micrantha.bluebell.ui.components.status.FailureContent
import com.micrantha.bluebell.ui.components.status.LoadingContent
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.ui.Skouter
import com.micrantha.skouter.ui.components.S
import com.micrantha.skouter.ui.dashboard.DashboardAction.Load
import com.micrantha.skouter.ui.dashboard.DashboardAction.ScanNewThing
import com.micrantha.skouter.ui.dashboard.DashboardUiState.Tabs
import com.micrantha.skouter.ui.dashboard.components.ScanNewThingCard
import com.micrantha.skouter.ui.game.components.GameListContent
import com.micrantha.skouter.ui.player.components.PlayerListContent
import com.micrantha.skouter.ui.thing.components.ThingListContent

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: DashboardScreenModel = rememberScreenModel()

        LaunchedEffect(viewModel) {
            viewModel.dispatch(Load)
        }

        val state by viewModel.state().collectAsState()

        Render(state, viewModel::dispatch)
    }

    @Composable
    fun Render(
        state: DashboardUiState,
        dispatch: Dispatch
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(
                        end = Dimensions.content
                    ).size(
                        MaterialTheme.typography.headlineLarge.fontSize.value.dp
                    ),
                    imageVector = Skouter.defaultIcon,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                )
                Text(
                    text = stringResource(S.AppTitle),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            ScanNewThingCard {
                dispatch(ScanNewThing)
            }

            when (state.status) {
                is Ready -> ContentPager(state.status.data, dispatch)
                is Failure -> FailureContent(state.status.message)
                else -> LoadingContent(stringResource(S.LoadingDashboard))
            }
        }
    }

    @Composable
    fun ContentPager(tabs: Tabs, dispatch: Dispatch) {
        TabPager(
            stringResource(S.Things),
            stringResource(S.Games),
            stringResource(S.Players)
        ) { page, _ ->
            when (page) {
                0 -> ThingListContent(tabs.things, dispatch)
                1 -> GameListContent(tabs.games, dispatch)
                2 -> PlayerListContent(tabs.players, dispatch)
            }
        }
    }
}
