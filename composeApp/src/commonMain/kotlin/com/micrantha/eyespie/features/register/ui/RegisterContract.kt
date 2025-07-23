package com.micrantha.eyespie.features.register.ui

import com.micrantha.bluebell.ui.model.UiResult
import org.jetbrains.compose.resources.StringResource

sealed interface RegisterAction {
    data class ChangedEmail(val email: String) : RegisterAction
    data class ChangedPassword(val password: String) : RegisterAction
    data class ChangedConfirmPassword(val confirm: String) : RegisterAction
    data object OnRegister : RegisterAction
    data object ResetStatus : RegisterAction
    data object OnSuccess : RegisterAction
    data class OnError(val error: Throwable): RegisterAction
    data object OnRegisterWithGoogle : RegisterAction
}

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val status: UiResult<Unit> = UiResult.Default
)

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val status: UiResult<Unit> = UiResult.Default,
    val error: StringResource? = null // Or your specific error type if not StringResource
) {
    val isValid: Boolean = email.isNotBlank() && password.isNotBlank() && password == confirmPassword && password.length >= 8 // Example validation
}