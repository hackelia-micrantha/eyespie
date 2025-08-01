package com.micrantha.eyespie.features.onboarding.ui

import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Effect
import com.micrantha.bluebell.arch.Reducer
import com.micrantha.bluebell.arch.StateMapper

class OnboardingEnvironment : Reducer<OnboardingState>, Effect<OnboardingState> {
    override fun reduce(
        state: OnboardingState,
        action: Action
    ): OnboardingState {
        TODO("Not yet implemented")
    }

    override suspend fun invoke(
        action: Action,
        state: OnboardingState
    ) {
        TODO("Not yet implemented")
    }

    fun map(state: OnboardingState) = OnboardingEnvironment.map(state)

    companion object : StateMapper<OnboardingState, OnboardingUiState> {
        override fun map(state: OnboardingState) = OnboardingUiState(
            page = state.page
        )
    }
}
