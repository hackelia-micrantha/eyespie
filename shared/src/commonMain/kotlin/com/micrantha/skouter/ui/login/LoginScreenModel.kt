package com.micrantha.skouter.ui.login

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

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
