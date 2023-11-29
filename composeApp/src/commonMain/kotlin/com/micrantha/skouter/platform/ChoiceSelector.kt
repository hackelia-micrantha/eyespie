package com.micrantha.skouter.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.skouter.ui.component.Choice

@Composable
expect fun ChoiceSelector(
    modifier: Modifier,
    active: Boolean,
    onDismiss: () -> Unit,
    choices: List<Choice>,
    onSelect: (Choice) -> Unit,
)

