package com.micrantha.eyespie.features.onboarding.ui

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class OnboardingScreenModel(
    context: ScreenContext,
    private val environment: OnboardingEnvironment,
    initialState: OnboardingState = OnboardingState()
) : MappedScreenModel<OnboardingState, OnboardingUiState>(
    context, initialState, environment::map
) {
    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
