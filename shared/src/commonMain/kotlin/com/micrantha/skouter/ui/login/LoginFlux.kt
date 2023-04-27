package com.micrantha.skouter.ui.login

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.i18n.StringResource
import com.micrantha.bluebell.domain.model.ResultStatus
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.arch.i18n

sealed class LoginAction : Action {

    object OnLogin : LoginAction()

    data class OnError(val err: Throwable) : LoginAction()

    object OnSuccess : LoginAction()

    data class ChangedPassword(val password: String) : LoginAction()

    data class ChangedEmail(val email: String) : LoginAction()
}

class LoginReducer(private val string: StringResource) : Reducer<LoginState> {
    override fun invoke(state: LoginState, action: Action): LoginState =
        when (action) {
            is LoginAction.ChangedEmail -> state.copy(email = action.email)
            is LoginAction.ChangedPassword -> state.copy(password = action.password)
            is LoginAction.OnLogin -> state.copy(status = ResultStatus.Busy(string(i18n.LoggingIn)))
            is LoginAction.OnError -> state.copy(status = ResultStatus.Failure(string(i18n.LoginFailed)))
            else -> state
        }
}

class LoginEffects(
    private val viewContext: ViewContext,
    private val environment: LoginEnvironment
) :
    Effect<LoginState>, Dispatcher by viewContext {
    override suspend fun invoke(action: Action, state: LoginState) {
        when (action) {
            is LoginAction.OnLogin -> environment.login(state.email, state.password).onFailure {
                dispatch(LoginAction.OnError(it))
            }.onSuccess {
                dispatch(LoginAction.OnSuccess)
            }
            is LoginAction.OnSuccess -> viewContext.navigate(environment.nextRoute())
        }
    }
}
