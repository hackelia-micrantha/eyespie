package com.micrantha.eyespie.features.register.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ext.enabled
import com.micrantha.bluebell.ui.components.StateRenderer
import com.micrantha.bluebell.ui.model.isFailure
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.app.EyesPie // Assuming this is your app's entry or common resources
import com.micrantha.eyespie.app.S // Your string resources
import eyespie.composeapp.generated.resources.confirm_password
import eyespie.composeapp.generated.resources.email
import eyespie.composeapp.generated.resources.login_email_placeholder
import eyespie.composeapp.generated.resources.login_password_placeholder
import eyespie.composeapp.generated.resources.login_with_google
import eyespie.composeapp.generated.resources.password
import eyespie.composeapp.generated.resources.register
import eyespie.composeapp.generated.resources.register_confirm_password_placeholder
import eyespie.composeapp.generated.resources.register_title
import eyespie.composeapp.generated.resources.register_with_google
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

// Add these to your S (string resources) or a similar generated file:
// res.string.register_title -> "Register"
// res.string.confirm_password -> "Confirm Password"
// res.string.register_confirm_password_placeholder -> "Re-enter your password"
// res.string.register -> "Register"
// res.string.passwords_do_not_match -> "Passwords do not match." // Example validation message
// res.string.password_too_short -> "Password must be at least 8 characters." // Example validation message

class RegisterScreen : Screen, StateRenderer<RegisterUiState> {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<RegisterScreenModel>()
        val state by screenModel.state.collectAsState()

        Render(state, screenModel)
    }

    @Composable
    override fun Render(state: RegisterUiState, dispatch: Dispatch) {
        Box(
            modifier = Modifier.fillMaxSize()
                .scrollable(rememberScrollState(), Orientation.Vertical),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(Dimensions.screen)
            ) {
                Icon(
                    EyesPie.defaultIcon, // Or a specific registration icon
                    contentDescription = null,
                    modifier = Modifier.size(Dimensions.List.placeholder),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(S.register_title), // Replace with actual resource
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(Dimensions.screen))

                TextField(
                    value = state.email,
                    enabled = state.status.enabled(),
                    maxLines = 1,
                    label = { Text(stringResource(S.email)) },
                    onValueChange = { dispatch(RegisterAction.ChangedEmail(it)) },
                    placeholder = { Text(stringResource(S.login_email_placeholder)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dimensions.content))

                TextField(
                    value = state.password,
                    enabled = state.status.enabled(),
                    visualTransformation = PasswordVisualTransformation(),
                    maxLines = 1,
                    label = { Text(stringResource(S.password)) },
                    onValueChange = { dispatch(RegisterAction.ChangedPassword(it)) },
                    placeholder = { Text(stringResource(S.login_password_placeholder)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dimensions.content))

                TextField(
                    value = state.confirmPassword,
                    enabled = state.status.enabled(),
                    visualTransformation = PasswordVisualTransformation(),
                    maxLines = 1,
                    label = { Text(stringResource(S.confirm_password)) }, // Add this string resource
                    onValueChange = { dispatch(RegisterAction.ChangedConfirmPassword(it)) },
                    placeholder = { Text(stringResource(S.register_confirm_password_placeholder)) }, // Add this
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.password.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(Dimensions.screen))

                ElevatedButton(
                    enabled = state.status.enabled() && state.isValid, // Consider isValid for button enablement
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(Dimensions.content),
                    onClick = { dispatch(RegisterAction.OnRegister) }) {
                    if (state.status.enabled()) {
                        Text(stringResource(S.register)) // Add this string resource
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(Dimensions.Image.button)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimensions.content))
                ElevatedButton(
                    enabled = state.status.enabled(),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(Dimensions.content),
                    onClick = { dispatch(RegisterAction.OnRegisterWithGoogle) }) {
                    Text(stringResource(S.register_with_google)) // Or a "Register with Google" string
                }

                Spacer(modifier = Modifier.height(Dimensions.screen))

                Column(modifier = Modifier.heightIn(80.dp)) {
                    AnimatedVisibility(
                        visible = state.status.isFailure,
                        enter = fadeIn(initialAlpha = 0.3f)
                    ) {
                        Messages(state = state, dispatch = dispatch)
                    }
                }
            }
        }
    }

    @Composable
    private fun Messages(state: RegisterUiState, dispatch: Dispatch) {
        state.error?.let { // Assuming error is a StringResource
            Text(
                text = stringResource(it),
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.error
            )

            LaunchedEffect(state.status) { // Relaunch if status changes to ensure reset
                if (state.status.isFailure) {
                    delay(5000)
                    dispatch(RegisterAction.ResetStatus)
                }
            }
        }
    }
}
