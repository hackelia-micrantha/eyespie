package com.micrantha.eyespie.features.players.ui.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.StateRenderer
import com.micrantha.bluebell.ui.model.UiResult
import com.micrantha.bluebell.ui.model.enabled
import com.micrantha.bluebell.ui.model.error
import com.micrantha.bluebell.ui.model.isFailure
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.core.ui.Screen
import com.micrantha.eyespie.features.login.ui.LoginAction.ResetStatus
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource


class NewPlayerScreen : Screen, StateRenderer<NewPlayerUiState> {
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<NewPlayerScreenModel>()

        val state by screenModel.state.collectAsState()

        Render(state, screenModel)
    }

    @Composable
    override fun Render(state: NewPlayerUiState, dispatch: Dispatch) {
        Column(
            modifier = Modifier.padding(Dimensions.Padding.mediumLarge),
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(modifier = Modifier.heightIn(Dimensions.screen * 2))
            Text("Create New Player", fontSize = Dimensions.Text.Large)
            Spacer(modifier = Modifier.heightIn(Dimensions.screen))

            TextField(
                singleLine = true,
                maxLines = 1,
                enabled = state.status.enabled(),
                isError = state.firstName.isError,
                value = state.firstName.value,
                onValueChange = {
                    dispatch(Action.ChangedFirstName(it))
                },
                label = { Text("First Name") },
                placeholder = { Text("Enter first name") },
                supportingText = {
                    state.firstName.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            Spacer(modifier = Modifier.heightIn(Dimensions.screen))

            TextField(
                singleLine = true,
                maxLines = 1,
                enabled = state.status.enabled(),
                value = state.lastName.value,
                isError = state.lastName.isError,
                onValueChange = {
                    dispatch(Action.ChangedLastName(it))
                },
                label = { Text("Last Name") },
                supportingText = {
                    state.lastName.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            Spacer(modifier = Modifier.heightIn(Dimensions.screen))

            TextField(
                singleLine = true,
                maxLines = 1,
                enabled = state.status.enabled(),
                value = state.nickName.value,
                isError = state.nickName.isError,
                supportingText = {
                    state.nickName.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                onValueChange = {
                    dispatch(Action.ChangedNickName(it))
                },
                label = { Text("Nick Name") }
            )
            ElevatedButton(
                onClick = {
                    dispatch(Action.OnSave)
                }
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.heightIn(Dimensions.screen))

            Column(modifier = Modifier.heightIn(80.dp)) {
                AnimatedVisibility(
                    visible = state.status.isFailure,
                    enter = fadeIn(
                        initialAlpha = 0.3f
                    )
                ) {
                    Messages(status = state.status, dispatch = dispatch)
                }
            }
        }
    }

    @Composable
    private fun Messages(status: UiResult<*>, dispatch: Dispatch) {
        status.error?.let {
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
