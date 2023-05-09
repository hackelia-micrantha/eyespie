package com.micrantha.skouter.ui.login

import Skouter.shared.BuildConfig
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.ui.navi.Routes

data class LoginState(
    val email: String = BuildConfig.userLoginEmail,
    val password: String = BuildConfig.userLoginPassword,
    val isLoggedIn: Boolean = false,
    val status: UiResult<Unit> = UiResult.Default
)

data class LoginUiState(
    val email: String,
    val password: String,
    val isLoggedIn: Boolean = false,
    val status: UiResult<Unit>
)

fun LoginState.asUiState() = LoginUiState(
    email = this.email,
    password = this.password,
    isLoggedIn = this.isLoggedIn,
    status = this.status
)

class LoginEnvironment(
    private val accountRepository: AccountRepository,
) {
    suspend fun login(email: String, password: String) = accountRepository.login(email, password)

    fun nextRoute() = Routes.Games
}

