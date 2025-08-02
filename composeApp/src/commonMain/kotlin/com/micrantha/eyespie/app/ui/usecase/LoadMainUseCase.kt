package com.micrantha.eyespie.app.ui.usecase

import com.micrantha.bluebell.app.Log
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.navigate
import com.micrantha.eyespie.domain.repository.AccountRepository
import com.micrantha.eyespie.features.login.ui.LoginScreen
import com.micrantha.eyespie.features.onboarding.data.OnboardingRepository
import com.micrantha.eyespie.features.onboarding.ui.OnboardingScreen
import com.micrantha.eyespie.features.players.domain.usecase.LoadSessionPlayerUseCase

class LoadMainUseCase(
    private val context: ScreenContext,
    private val accountRepository: AccountRepository,
    private val loadSessionPlayerUseCase: LoadSessionPlayerUseCase,
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke() = try {
        if (onboardingRepository.hasRunOnce().not()) {
            context.navigate<OnboardingScreen>(Router.Options.Replace)
        } else {

        accountRepository.session().onFailure {
            context.navigate<LoginScreen>(Router.Options.Replace)
        }.onSuccess { session ->
            loadSessionPlayerUseCase.withNavigation(session)
        } }
    } catch (err: Throwable){
        Log.e("main", err){"unexpected error"}
    }
}
