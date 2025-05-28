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
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.app.EyesPie
import com.micrantha.eyespie.app.S
import eyespie.composeapp.generated.resources.name
import eyespie.composeapp.generated.resources.new_game_content
import eyespie.composeapp.generated.resources.new_game_header
import eyespie.composeapp.generated.resources.next
import org.jetbrains.compose.resources.stringResource

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
                    text = stringResource(S.new_game_header),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(S.new_game_content),
                    style = MaterialTheme.typography.bodyLarge
                )

                TextField(
                    value = state.name,
                    label = {
                        Text(
                            text = stringResource(S.name),
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
                    Text(text = stringResource(S.next))
                }
            }
        }
}
