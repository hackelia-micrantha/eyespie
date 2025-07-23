package com.micrantha.eyespie.ui.login

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.eyespie.features.login.ui.LoginEnvironment
import com.micrantha.eyespie.features.login.ui.LoginState
import com.micrantha.eyespie.features.login.ui.LoginUiState

class LoginScreenModel(
    screenContext: ScreenContext,
    environment: LoginEnvironment,
    initialState: LoginState = LoginState()
) : MappedScreenModel<LoginState, LoginUiState>(
    screenContext,
    initialState,
    environment::map
) {

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
