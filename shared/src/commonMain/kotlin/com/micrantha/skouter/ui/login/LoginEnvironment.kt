package com.micrantha.skouter.ui.login

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.UiResult.Default
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.bluebell.domain.model.busy
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.get
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.ui.MainAction.Load
import com.micrantha.skouter.ui.components.Strings
import com.micrantha.skouter.ui.games.list.GameListScreen
import com.micrantha.skouter.ui.login.LoginAction.ChangedEmail
import com.micrantha.skouter.ui.login.LoginAction.ChangedPassword
import com.micrantha.skouter.ui.login.LoginAction.Loaded
import com.micrantha.skouter.ui.login.LoginAction.OnError
import com.micrantha.skouter.ui.login.LoginAction.OnLogin
import com.micrantha.skouter.ui.login.LoginAction.OnSuccess

class LoginEnvironment(
    private val context: ScreenContext,
    private val dispatcher: Dispatcher,
    private val localizedRepository: LocalizedRepository,
    private val accountRepository: AccountRepository,
) : Reducer<LoginState>, Effect<LoginState>, Dispatcher by dispatcher,
    LocalizedRepository by localizedRepository, Router by context {

    fun map(state: LoginState) = LoginUiState(
        email = state.email,
        password = state.password,
        status = state.status
    )

    override fun reduce(state: LoginState, action: Action) = when (action) {
        is ChangedEmail -> state.copy(email = action.email)
        is ChangedPassword -> state.copy(password = action.password)
        is OnLogin -> state.copy(status = busy(Strings.LoggingIn))
        is OnError -> state.copy(status = error(Strings.LoginFailed))
        is Loaded -> state.copy(status = Ready(action.isLoggedIn))
        else -> state
    }

    override suspend fun invoke(action: Action, state: LoginState) {
        when (action) {
            is Load -> if (state.status is Default) {
                dispatch(Loaded(accountRepository.isLoggedIn()))
            }
            is OnLogin -> accountRepository.login(state.email, state.password)
                .onFailure {
                    dispatch(OnError(it))
                }.onSuccess {
                    dispatch(OnSuccess)
                }
            is Loaded -> if (action.isLoggedIn) {
                navigate<GameListScreen>(context.get())
            }
            is OnSuccess -> navigate<GameListScreen>(context.get())
        }
    }
}

