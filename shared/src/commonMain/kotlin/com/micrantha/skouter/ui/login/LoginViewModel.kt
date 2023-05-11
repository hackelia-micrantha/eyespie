package com.micrantha.skouter.ui.login

import com.micrantha.bluebell.ui.view.MappedViewModel
import com.micrantha.bluebell.ui.view.ViewContext

class LoginViewModel(
    viewContext: ViewContext,
    environment: LoginEnvironment
) :
    MappedViewModel<LoginState, LoginUiState>(
        viewContext,
        LoginState(),
        LoginState::asUiState
    ) {

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
