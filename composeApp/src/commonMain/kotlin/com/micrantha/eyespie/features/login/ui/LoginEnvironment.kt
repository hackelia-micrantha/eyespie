package com.micrantha.eyespie.features.login.ui

import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.arch.Effect
import com.micrantha.bluebell.arch.Reducer
import com.micrantha.bluebell.arch.StateMapper
import com.micrantha.bluebell.domain.repository.LocalizedRepository
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.model.UiResult.Busy
import com.micrantha.bluebell.ui.model.UiResult.Default
import com.micrantha.bluebell.ui.model.UiResult.Failure
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.navigate
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.domain.repository.AccountRepository
import com.micrantha.eyespie.features.login.ui.LoginAction.ChangedEmail
import com.micrantha.eyespie.features.login.ui.LoginAction.ChangedPassword
import com.micrantha.eyespie.features.login.ui.LoginAction.OnError
import com.micrantha.eyespie.features.login.ui.LoginAction.OnLogin
import com.micrantha.eyespie.features.login.ui.LoginAction.OnLoginWithGoogle
import com.micrantha.eyespie.features.login.ui.LoginAction.OnRegister
import com.micrantha.eyespie.features.login.ui.LoginAction.OnSuccess
import com.micrantha.eyespie.features.login.ui.LoginAction.ResetStatus
import com.micrantha.eyespie.features.login.ui.LoginAction.ToggleEmailMask
import com.micrantha.eyespie.features.login.ui.LoginAction.TogglePasswordMask
import com.micrantha.eyespie.features.players.domain.usecase.LoadSessionPlayerUseCase
import com.micrantha.eyespie.features.register.ui.RegisterScreen
import eyespie.composeapp.generated.resources.logging_in
import eyespie.composeapp.generated.resources.login_failed

class LoginEnvironment(
    private val context: ScreenContext,
    private val accountRepository: AccountRepository,
    private val loadPlayerUseCase: LoadSessionPlayerUseCase,
) : Reducer<LoginState>, Effect<LoginState>,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n, Router by context.router {

    companion object : StateMapper<LoginState, LoginUiState> {
        private var uiState: LoginUiState? = null

        override fun map(state: LoginState) = uiState?.copy(
            email = state.email,
            password = state.password,
            status = state.status,
            isEmailMasked = state.isEmailMasked ?: state.email.isNotBlank(),
            isPasswordMasked = state.isPasswordMasked
        ) ?: LoginUiState(
            email = state.email,
            password = state.password,
            status = state.status,
            isEmailMasked = state.isEmailMasked ?: state.email.isNotBlank(),
            isPasswordMasked = state.isPasswordMasked
        ).also {
            uiState = it
        }
    }

    fun map(state: LoginState) = LoginEnvironment.map(state)

    override fun reduce(state: LoginState, action: Action) = when (action) {
        is ChangedEmail -> state.copy(email = action.email)
        is ChangedPassword -> state.copy(password = action.password)
        is OnSuccess -> state.copy(status = Default)
        is ToggleEmailMask -> state.copy(isEmailMasked = state.isEmailMasked?.not() ?: state.email.isBlank())
        is TogglePasswordMask -> state.copy(isPasswordMasked = !state.isPasswordMasked)
        is OnLogin, is OnLoginWithGoogle -> state.copy(status = Busy(S.logging_in))
        is OnError -> state.copy(status = Failure(S.login_failed))
        is ResetStatus -> state.copy(status = Default)
        else -> state
    }

    override suspend fun invoke(action: Action, state: LoginState) {
        when (action) {
            is OnLogin -> accountRepository.login(state.email, state.password)
                .onFailure {
                    dispatch(OnError(it))
                }.onSuccess {
                    dispatch(OnSuccess(it))
                }

            is OnLoginWithGoogle -> accountRepository.loginWithGoogle()
                .onSuccess {
                    dispatch(OnSuccess(it))
                }.onFailure {
                    dispatch(OnError(it))
                }

            is OnRegister -> context.navigate<RegisterScreen>()

            is OnSuccess -> loadPlayerUseCase.withNavigation(action.session) {
                dispatch(OnError(it))
            }
        }
    }
}

