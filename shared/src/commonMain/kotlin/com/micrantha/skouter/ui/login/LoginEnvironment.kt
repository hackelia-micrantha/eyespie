package com.micrantha.skouter.ui.login

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.busy
import com.micrantha.bluebell.ui.navi.Router
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.ui.components.i18n
import com.micrantha.skouter.ui.navi.Routes

class LoginEnvironment(
    private val router: Router,
    private val dispatcher: Dispatcher,
    private val localizedRepository: LocalizedRepository,
    private val accountRepository: AccountRepository,
) : Reducer<LoginState>, Effect<LoginState>, Dispatcher by dispatcher,
    LocalizedRepository by localizedRepository {

    override fun reduce(state: LoginState, action: Action) = when (action) {
        is LoginAction.ChangedEmail -> state.copy(email = action.email)
        is LoginAction.ChangedPassword -> state.copy(password = action.password)
        is LoginAction.OnLogin -> state.copy(status = busy(i18n.LoggingIn))
        is LoginAction.OnError -> state.copy(status = error(i18n.LoginFailed))
        else -> state
    }

    override suspend fun invoke(action: Action, state: LoginState) {
        when (action) {
            is LoginAction.OnLogin -> accountRepository.login(state.email, state.password)
                .onFailure {
                    dispatch(LoginAction.OnError(it))
                }.onSuccess {
                    dispatch(LoginAction.OnSuccess)
                }
            is LoginAction.OnSuccess -> router.navigate(Routes.Games)
        }
    }
}

