package com.micrantha.skouter.platform

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.skouter.ui.component.Choice

@Composable
actual fun ChoiceSelector(
    modifier: Modifier,
    active: Boolean,
    onDismiss: () -> Unit,
    choices: List<Choice>,
    onSelect: (Choice) -> Unit,
) {
    DropdownMenu(expanded = active, onDismissRequest = onDismiss) {
        choices.forEachIndexed { index, choice ->
            DropdownMenuItem(text = {
                Text(choice.label)
            }, onClick = { onSelect(choice) })
        }
    }
}
