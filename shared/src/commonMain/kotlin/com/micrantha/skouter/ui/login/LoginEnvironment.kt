package com.micrantha.skouter.ui.login

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.ext.busy
import com.micrantha.bluebell.domain.ext.failure
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.UiResult.Default
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.components.navigate
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.StateMapper
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.ui.component.Strings
import com.micrantha.skouter.ui.dashboard.DashboardScreen
import com.micrantha.skouter.ui.login.LoginAction.ChangedEmail
import com.micrantha.skouter.ui.login.LoginAction.ChangedPassword
import com.micrantha.skouter.ui.login.LoginAction.OnError
import com.micrantha.skouter.ui.login.LoginAction.OnLogin
import com.micrantha.skouter.ui.login.LoginAction.OnSuccess

class LoginEnvironment(
    private val context: ScreenContext,
    private val accountRepository: AccountRepository,
) : Reducer<LoginState>, Effect<LoginState>,
    StateMapper<LoginState, LoginUiState>,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n, Router by context.router {

    override fun map(state: LoginState) = LoginUiState(
        email = state.email,
        password = state.password,
        status = state.status
    )

    override fun reduce(state: LoginState, action: Action) = when (action) {
        is ChangedEmail -> state.copy(email = action.email)
        is ChangedPassword -> state.copy(password = action.password)
        is OnSuccess -> state.copy(status = Default)
        is OnLogin -> state.copy(status = busy(Strings.LoggingIn))
        is OnError -> state.copy(status = failure(Strings.LoginFailed))
        else -> state
    }

    override suspend fun invoke(action: Action, state: LoginState) {
        when (action) {

            is OnLogin -> accountRepository.login(state.email, state.password)
                .onFailure {
                    dispatch(OnError(it))
                }.onSuccess {
                    dispatch(OnSuccess)
                }
            is OnSuccess -> navigate<DashboardScreen>()
        }
    }
}

