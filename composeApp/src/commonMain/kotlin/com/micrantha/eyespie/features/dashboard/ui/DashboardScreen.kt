package com.micrantha.eyespie.features.dashboard.ui

import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.stringResource
import com.micrantha.bluebell.ui.model.UiResult.Failure
import com.micrantha.bluebell.ui.model.UiResult.Ready
import com.micrantha.bluebell.ui.components.TabPager
import com.micrantha.bluebell.ui.components.status.FailureContent
import com.micrantha.bluebell.ui.components.status.LoadingContent
import com.micrantha.eyespie.core.ui.component.AppTitle
import com.micrantha.eyespie.core.ui.component.LocationEnabledEffect
import com.micrantha.eyespie.core.ui.component.RealtimeDataEnabledEffect
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.Load
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.ScanNewThing
import com.micrantha.eyespie.features.dashboard.ui.component.FriendsTabContent
import com.micrantha.eyespie.features.dashboard.ui.component.NearbyTabContent
import com.micrantha.eyespie.features.dashboard.ui.component.ScanNewThingCard

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel: DashboardScreenModel = rememberScreenModel()

        LaunchedEffect(Unit) {
            screenModel.dispatch(Load)
        }

        LocationEnabledEffect()

        RealtimeDataEnabledEffect()

        val state by screenModel.state.collectAsState()

        Render(state, screenModel)
    }

    @Composable
    fun Render(
        state: DashboardUiState,
        dispatch: Dispatch
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().scrollable(rememberScrollState(), Vertical)
        ) {

            AppTitle()

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
    fun ContentPager(data: DashboardUiState.Data, dispatch: Dispatch) {
        TabPager(
            stringResource(S.Nearby),
            stringResource(S.Friends)
        ) { page, _ ->
            when (page) {
                0 -> NearbyTabContent(data.nearby, dispatch)
                1 -> FriendsTabContent(data.friends, dispatch)
            }
        }
    }
}
