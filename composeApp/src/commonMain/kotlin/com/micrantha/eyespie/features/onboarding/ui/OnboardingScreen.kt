package com.micrantha.eyespie.features.onboarding.ui

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.StateRenderer
import com.micrantha.eyespie.core.ui.Screen

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
        TODO("Not yet implemented")
    }

    @Composable
    override fun Render(
        state: OnboardingUiState,
        dispatch: Dispatch
    ) {
        TODO("Not yet implemented")
    }
}
