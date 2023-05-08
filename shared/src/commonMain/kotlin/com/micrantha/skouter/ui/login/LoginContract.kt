package com.micrantha.skouter.ui.login

import Skouter.shared.BuildConfig
import com.micrantha.bluebell.domain.model.ResultStatus
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.ui.navi.Routes

data class LoginState(
    val email: String = BuildConfig.loginEmail,
    val password: String = BuildConfig.loginPassword,
    val isLoggedIn: Boolean = false,
    val status: ResultStatus<Unit> = ResultStatus.Default
)

data class LoginUiState(
    val email: String,
    val password: String,
    val isLoggedIn: Boolean = false,
    val status: ResultStatus<Unit>
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

