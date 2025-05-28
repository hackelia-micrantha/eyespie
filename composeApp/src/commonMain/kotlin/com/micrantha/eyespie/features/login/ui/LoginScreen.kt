package com.micrantha.eyespie.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.micrantha.bluebell.ui.model.error
import com.micrantha.bluebell.ui.model.isFailure
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.app.EyesPie
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.ui.login.LoginAction.ResetStatus
import eyespie.composeapp.generated.resources.app_title
import eyespie.composeapp.generated.resources.email
import eyespie.composeapp.generated.resources.login
import eyespie.composeapp.generated.resources.login_email_placeholder
import eyespie.composeapp.generated.resources.login_password_placeholder
import eyespie.composeapp.generated.resources.password
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

class LoginScreen : Screen, StateRenderer<LoginUiState> {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<LoginScreenModel>()

        val state by screenModel.state.collectAsState()

        Render(state, screenModel)
    }

    @Composable
    override fun Render(state: LoginUiState, dispatch: Dispatch) {

        Box(
            modifier = Modifier.fillMaxSize()
                .scrollable(rememberScrollState(), Vertical), contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(Dimensions.screen)
            ) {
                Icon(
                    EyesPie.defaultIcon,
                    contentDescription = null,
                    modifier = Modifier.size(Dimensions.List.placeholder),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(S.app_title),
                    fontSize = Dimensions.Text.Large
                )

                Spacer(modifier = Modifier.heightIn(Dimensions.screen))

                TextField(
                    value = state.email,
                    enabled = state.status.enabled(),
                    maxLines = 1,
                    label = { Text(stringResource(S.email)) },
                    onValueChange = { dispatch(LoginAction.ChangedEmail(it)) },
                    placeholder = { Text(stringResource(S.login_email_placeholder)) }
                )

                Spacer(modifier = Modifier.heightIn(Dimensions.content))

                TextField(
                    enabled = state.status.enabled(),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.padding(Dimensions.content),
                    maxLines = 1,
                    label = { Text(stringResource(S.password)) },
                    value = state.password,
                    onValueChange = { dispatch(LoginAction.ChangedPassword(it)) },
                    placeholder = { Text(stringResource(S.login_password_placeholder)) }
                )

                Spacer(modifier = Modifier.heightIn(Dimensions.screen))

                ElevatedButton(
                    enabled = state.status.enabled(),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(Dimensions.content),
                    onClick = { dispatch(LoginAction.OnLogin) }) {
                    if (state.status.enabled()) {
                        Text(stringResource(S.login))
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(Dimensions.Image.button)
                        )
                    }
                }

                Spacer(modifier = Modifier.heightIn(Dimensions.screen))

                Column(modifier = Modifier.heightIn(80.dp)) {
                    AnimatedVisibility(
                        visible = state.status.isFailure,
                        enter = fadeIn(
                            initialAlpha = 0.3f
                        )
                    ) {
                        Messages(state = state, dispatch = dispatch)
                    }
                }
            }
        }
    }

    @Composable
    private fun Messages(state: LoginUiState, dispatch: Dispatch) {
        state.status.error?.let {
            Text(
                text = stringResource(it),
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.error
            )

            LaunchedEffect(Unit) {
                delay(5000)
                dispatch(ResetStatus)
            }
        }
    }
}
