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
import com.micrantha.skouter.ui.component.LocationEnabledEffect
import com.micrantha.skouter.ui.component.RealtimeDataEffect
import com.micrantha.skouter.ui.component.S
import com.micrantha.skouter.ui.dashboard.DashboardAction.Load
import com.micrantha.skouter.ui.dashboard.DashboardAction.ScanNewThing
import com.micrantha.skouter.ui.dashboard.component.FriendsTabContent
import com.micrantha.skouter.ui.dashboard.component.NearbyTabContent
import com.micrantha.skouter.ui.dashboard.component.ScanNewThingCard

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel: DashboardContextualScreenModel = rememberScreenModel()

        LocationEnabledEffect()

        RealtimeDataEffect()

        LaunchedEffect(Unit) {
            screenModel.dispatch(Load)
        }

        val state by screenModel.state.collectAsState()

        Render(state, screenModel::dispatch)
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
