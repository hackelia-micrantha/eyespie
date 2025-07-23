package com.micrantha.eyespie.features.register.ui

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
import com.micrantha.eyespie.features.dashboard.ui.DashboardScreen
import eyespie.composeapp.generated.resources.logging_in
import eyespie.composeapp.generated.resources.register_failed

class RegisterEnvironment(
    private val context: ScreenContext,
    private val accountRepository: AccountRepository,
) : Reducer<RegisterState>, Effect<RegisterState>,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n, Router by context.router {

    companion object : StateMapper<RegisterState, RegisterUiState> {

        private lateinit var uiState: RegisterUiState

        override fun map(state: RegisterState) = if (!::uiState.isInitialized) RegisterUiState(
            email = state.email,
            password = state.password,
            confirmPassword = state.confirmPassword,
            status = state.status
        ) else uiState.copy(email = state.email, password = state.password, confirmPassword = state.confirmPassword, status = state.status)
    }

    override fun reduce(state: RegisterState, action: Action) = when (action) {
        is RegisterAction.ChangedEmail -> state.copy(email = action.email)
        is RegisterAction.ChangedPassword -> state.copy(password = action.password)
        is RegisterAction.ChangedConfirmPassword -> state.copy(confirmPassword = action.confirm)
        is RegisterAction.OnSuccess -> state.copy(status = Default)
        is RegisterAction.OnRegister, is RegisterAction.OnRegisterWithGoogle -> state.copy(status = Busy(S.logging_in))
        is RegisterAction.OnError -> state.copy(status = Failure(S.register_failed))
        is RegisterAction.ResetStatus -> state.copy(status = Default)
        else -> state
    }

    override suspend fun invoke(action: Action, state: RegisterState) {
        when (action) {

            is RegisterAction.OnRegister -> accountRepository.register(state.email, state.password)
                .onFailure {
                    dispatch(RegisterAction.OnError(it))
                }.onSuccess {
                    dispatch(RegisterAction.OnSuccess)
                }

            is RegisterAction.OnRegisterWithGoogle -> accountRepository.registerWithGoogle()
                .onSuccess {
                    dispatch(RegisterAction.OnSuccess)
                }.onFailure {
                    dispatch(RegisterAction.OnError(it))
                }

            is RegisterAction.OnSuccess -> context.navigate<DashboardScreen>()
        }
    }
}