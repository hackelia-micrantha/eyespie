package com.micrantha.eyespie.features.game.ui.create

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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.arch.Dispatch
import com.micrantha.bluebell.ui.components.stringResource
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.app.Strings
import com.micrantha.eyespie.ui.EyesPie

class GameCreateScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<GameCreateScreenModel>()
        val state by screenModel.state.collectAsState()

        Render(state, screenModel)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Render(
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
                    imageVector = EyesPie.defaultIcon,
                    contentDescription = null,
                    modifier = Modifier.size(Dimensions.List.placeholder),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(Strings.NewGameHeader),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(Strings.NewGameContent),
                    style = MaterialTheme.typography.bodyLarge
                )

                TextField(
                    value = state.name,
                    label = {
                        Text(
                            text = stringResource(Strings.Name),
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
                    Text(text = stringResource(Strings.Next))
                }
            }
        }
}
