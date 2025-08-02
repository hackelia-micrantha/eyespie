package com.micrantha.eyespie.features.onboarding

import com.micrantha.bluebell.get
import com.micrantha.eyespie.features.onboarding.data.OnboardingLocalSource
import com.micrantha.eyespie.features.onboarding.data.OnboardingRepository
import com.micrantha.eyespie.features.onboarding.ui.OnboardingEnvironment
import com.micrantha.eyespie.features.onboarding.ui.OnboardingScreen
import com.micrantha.eyespie.features.onboarding.ui.OnboardingScreenModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf

internal fun onboardingModule() = DI.Module("Onboarding") {
    bindProviderOf(::OnboardingLocalSource)
    bindProviderOf(::OnboardingRepository)

    bindProviderOf(::OnboardingEnvironment)
    bindProviderOf(::OnboardingScreen)
    bindProvider { OnboardingScreenModel(get(), get()) }
}
