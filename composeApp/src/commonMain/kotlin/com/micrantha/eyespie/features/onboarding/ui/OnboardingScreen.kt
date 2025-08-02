package com.micrantha.eyespie.features.onboarding.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.StateRenderer
import com.micrantha.eyespie.core.ui.Screen
import com.micrantha.eyespie.features.onboarding.ui.components.ClickableAnimatedPagerIndicator

// TODO: wizard like setup for first run
// A) How to Play
//   1) Scanning
//   2) Sharing
//   3) Guessing
// B) Permissions Info
//   1) Camera for scanning
//   2) Notifications for game events and downloads
//   3) Storage for GenAI
//   4) Contacts for sharing
// C) GenAI
//   1) prompt user to download genAI models
//   2) start background downloads
//   3) notify user progress and completion
// D) Social
//   1) Invitations / contacts
//   2) Link social media share
//   3) More info in settings
class OnboardingScreen : Screen, StateRenderer<OnboardingUiState> {
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<OnboardingScreenModel>()

        val state by screenModel.state.collectAsState()

        Render(state, screenModel)
    }

    enum class OnboardingPage{
        GenAI
    }

    @Composable
    override fun Render(
        state: OnboardingUiState,
        dispatch: Dispatch
    ) {
        val pagerState=rememberPagerState(
            initialPage = 0,
            pageCount = {OnboardingPage.entries.size}
        )

        HorizontalPager(
            state=pagerState,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column {
                    when (it) {
                        OnboardingPage.GenAI.ordinal -> RenderGenAI(state, dispatch)
                    }
                }

                ClickableAnimatedPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                )
            }
        }
    }

    @Composable
    private fun ColumnScope.RenderGenAI(state: OnboardingUiState, dispatch: Dispatch){
        Text("Download artificial intelligence models?  Models help improve gameplay and experience.")

        Row {
            OutlinedButton({
                dispatch(OnboardingAction.Next)
            }) {
                Text("No")
            }
            OutlinedButton({dispatch(OnboardingAction.Download)}) {
                Text("Yes")
            }
        }
    }
}
