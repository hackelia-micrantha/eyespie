package com.micrantha.eyespie.ui.login

import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.arch.Effect
import com.micrantha.bluebell.arch.Reducer
import com.micrantha.bluebell.arch.StateMapper
import com.micrantha.bluebell.ext.busy
import com.micrantha.bluebell.ext.failure
import com.micrantha.bluebell.domain.repository.LocalizedRepository
import com.micrantha.bluebell.ui.model.UiResult.Default
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.navigate
import com.micrantha.eyespie.app.Strings
import com.micrantha.eyespie.domain.repository.AccountRepository
import com.micrantha.eyespie.features.dashboard.ui.DashboardScreen
import com.micrantha.eyespie.ui.login.LoginAction.ChangedEmail
import com.micrantha.eyespie.ui.login.LoginAction.ChangedPassword
import com.micrantha.eyespie.ui.login.LoginAction.OnError
import com.micrantha.eyespie.ui.login.LoginAction.OnLogin
import com.micrantha.eyespie.ui.login.LoginAction.OnSuccess
import com.micrantha.eyespie.ui.login.LoginAction.ResetStatus

class LoginEnvironment(
    private val context: ScreenContext,
    private val accountRepository: AccountRepository,
) : Reducer<LoginState>, Effect<LoginState>,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n, Router by context.router {

    companion object : StateMapper<LoginState, LoginUiState> {

        private lateinit var uiState: LoginUiState

        override fun map(state: LoginState) = if (!::uiState.isInitialized) LoginUiState(
            email = state.email,
            password = state.hash,
            status = state.status
        ) else uiState.copy(email = state.email, password = state.hash, status = state.status)
    }

    override fun reduce(state: LoginState, action: Action) = when (action) {
        is ChangedEmail -> state.copy(email = action.email)
        is ChangedPassword -> state.copy(hash = action.password)
        is OnSuccess -> state.copy(status = Default)
        is OnLogin -> state.copy(status = busy(Strings.LoggingIn))
        is OnError -> state.copy(status = failure(Strings.LoginFailed))
        is ResetStatus -> state.copy(status = Default)
        else -> state
    }

    override suspend fun invoke(action: Action, state: LoginState) {
        when (action) {

            is OnLogin -> accountRepository.login(state.email, state.hash)
                .onFailure {
                    dispatch(OnError(it))
                }.onSuccess {
                    dispatch(OnSuccess)
                }

            is OnSuccess -> context.navigate<DashboardScreen>()
        }
    }
}

