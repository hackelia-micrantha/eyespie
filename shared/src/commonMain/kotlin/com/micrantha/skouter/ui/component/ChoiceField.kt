package com.micrantha.skouter.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.micrantha.skouter.platform.ChoiceSelector

data class Choice(
    val label: String,
    val tag: String,
)

@Composable
fun ChoiceField(
    selected: @Composable (Choice) -> Unit,
    choices: List<Choice>,
    onSelect: (Choice) -> Unit,
) {
    val active by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf(choices.first()) }

    selected(current)

    ChoiceSelector(
        modifier = Modifier,
        active = active,
        onDismiss = {
            onSelect(current)
        },
        choices = choices,
        onSelect = { choice ->
            current = choice
        },
    )
}
