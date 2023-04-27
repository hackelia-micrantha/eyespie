package com.micrantha.skouter.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.domain.model.enabled
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.ui.arch.i18n

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.state().collectAsState()

    LoginContent(state, viewModel::dispatch)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(state: LoginUiState, dispatch: Dispatch) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Visibility,
                contentDescription = null,
                modifier = Modifier.size(Dimensions.Icon.placeholder),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(i18n.AppTitle),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = Dimensions.Text.Large)
            )

            Spacer(modifier = Modifier.heightIn(Dimensions.screen))

            TextField(
                modifier = Modifier.padding(Dimensions.content),
                value = state.email,
                enabled = state.status.enabled(),
                maxLines = 1,
                label = { Text(stringResource(i18n.Email)) },
                onValueChange = { dispatch(LoginAction.ChangedEmail(it)) },
                placeholder = { Text(stringResource(i18n.LoginEmailPlaceholder)) }
            )
            TextField(
                enabled = state.status.enabled(),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(Dimensions.content),
                maxLines = 1,
                label = { Text(stringResource(i18n.Password)) },
                value = state.password,
                onValueChange = { dispatch(LoginAction.ChangedPassword(it)) },
                placeholder = { Text(stringResource(i18n.LoginPasswordPlaceholder)) }
            )

            OutlinedButton(
                enabled = state.status.enabled(),
                modifier = Modifier.fillMaxWidth().padding(Dimensions.screen),
                contentPadding = PaddingValues(Dimensions.content),
                onClick = { dispatch(LoginAction.OnLogin) }) {
                Text(stringResource(i18n.Login))
            }
        }
    }
}
