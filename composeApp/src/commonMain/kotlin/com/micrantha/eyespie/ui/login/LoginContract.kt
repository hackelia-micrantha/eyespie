package com.micrantha.eyespie.ui.login

import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.eyespie.AppConfig

data class LoginState(
    val email: String = AppConfig.LOGIN_EMAIL,
    val hash: String = AppConfig.LOGIN_PASSWORD,
    val status: UiResult<Unit> = UiResult.Default
)

data class LoginUiState(
    val email: String,
    val password: String,
    val status: UiResult<Unit>
)


sealed interface LoginAction {

    data object OnLogin : LoginAction

    data class OnError(val err: Throwable) : LoginAction

    data object OnSuccess : LoginAction

    data class ChangedPassword(val password: String) : LoginAction

    data class ChangedEmail(val email: String) : LoginAction

    data object ResetStatus : LoginAction
}
