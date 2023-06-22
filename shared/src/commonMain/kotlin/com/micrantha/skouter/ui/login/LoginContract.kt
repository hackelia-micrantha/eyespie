package com.micrantha.skouter.ui.login

import com.micrantha.bluebell.data.hash
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.UiResult

data class LoginState(
    val email: String = "",//BuildConfig.userLoginEmail,
    val hash: String = "",//hash(BuildConfig.userLoginPassword),
    val status: UiResult<Unit> = UiResult.Default
)

data class LoginUiState(
    val email: String,
    val password: String,
    val status: UiResult<Unit>
)


sealed class LoginAction : Action {

    object OnLogin : LoginAction()

    data class OnError(val err: Throwable) : LoginAction()

    object OnSuccess : LoginAction()

    data class ChangedPassword(val password: String) : LoginAction()

    data class ChangedEmail(val email: String) : LoginAction()
}
