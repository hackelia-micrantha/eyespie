package com.micrantha.eyespie.features.onboarding.ui

data class OnboardingState(
    val page: Int = 0
)

data class OnboardingUiState(
    val page: Int
)

sealed interface OnboardingAction {
    data object Next : OnboardingAction
}
