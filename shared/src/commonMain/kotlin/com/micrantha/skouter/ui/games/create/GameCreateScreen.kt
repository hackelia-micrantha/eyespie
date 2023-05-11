package com.micrantha.skouter.ui.games.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.ui.Skouter
import com.micrantha.skouter.ui.components.i18n

@Composable
fun GameCreateScreen(
    viewModel: GameCreateViewModel
) {
    val state by viewModel.state().collectAsState()

    GameCreateContent(state, viewModel::dispatch)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameCreateContent(
    state: GameCreateState,
    dispatch: Dispatch
) =
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Skouter.defaultIcon,
                contentDescription = null,
                modifier = Modifier.size(Dimensions.List.placeholder),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(i18n.NewGameHeader),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(i18n.NewGameContent),
                style = MaterialTheme.typography.bodyLarge
            )

            TextField(
                value = state.name,
                label = {
                    Text(
                        text = stringResource(i18n.Name),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                onValueChange = {
                    dispatch(GameCreateAction.NameChanged(it))
                }
            )

            Button(
                onClick = { dispatch(GameCreateAction.NameDone) }
            ) {
                Text(text = stringResource(i18n.Next))
            }
        }
    }
