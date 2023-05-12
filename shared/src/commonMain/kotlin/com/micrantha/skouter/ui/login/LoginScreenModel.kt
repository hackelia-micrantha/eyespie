package com.micrantha.skouter.ui.login

import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.ScreenMappedModel

class LoginScreenModel(
    screenContext: ScreenContext,
    environment: LoginEnvironment,
    initialState: LoginState = LoginState()
) : ScreenMappedModel<LoginState, LoginUiState>(
    screenContext,
    initialState,
    environment::map
) {

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
