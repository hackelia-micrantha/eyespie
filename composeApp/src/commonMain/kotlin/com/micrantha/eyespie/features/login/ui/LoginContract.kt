package com.micrantha.eyespie.features.login.ui

import com.micrantha.bluebell.ui.model.UiResult
import com.micrantha.eyespie.app.AppConfig
import com.micrantha.eyespie.domain.entities.Session

data class LoginState(
    val email: String = AppConfig.LOGIN_EMAIL,
    val password: String = AppConfig.LOGIN_PASSWORD,
    val status: UiResult<Unit> = UiResult.Default,
    val isPasswordMasked: Boolean = true,
    val isEmailMasked: Boolean? = null
)

data class LoginUiState(
    val email: String,
    val password: String,
    val status: UiResult<Unit>,
    val isEmailMasked: Boolean,
    val isPasswordMasked: Boolean
)

sealed interface LoginAction {

    data object OnLogin : LoginAction

    data object OnLoginWithGoogle : LoginAction

    data class OnError(val err: Throwable) : LoginAction

    data class OnSuccess(val session: Session) : LoginAction

    data object ToggleEmailMask : LoginAction

    data object TogglePasswordMask : LoginAction

    data class ChangedPassword(val password: String) : LoginAction

    data class ChangedEmail(val email: String) : LoginAction

    data object ResetStatus : LoginAction

    data object OnRegister : LoginAction
}
