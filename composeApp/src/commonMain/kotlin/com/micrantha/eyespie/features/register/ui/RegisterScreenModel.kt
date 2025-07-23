package com.micrantha.eyespie.features.register.ui

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class RegisterScreenModel(
    screenContext: ScreenContext,
    environment: RegisterEnvironment,
    initialState: RegisterState = RegisterState(),
) : MappedScreenModel<RegisterState, RegisterUiState>(
    screenContext,
    initialState,
    RegisterEnvironment::map
) {

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}