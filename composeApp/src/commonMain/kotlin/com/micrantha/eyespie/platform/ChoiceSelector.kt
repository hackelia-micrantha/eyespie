package com.micrantha.eyespie.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.eyespie.core.ui.component.Choice

@Composable
expect fun ChoiceSelector(
    modifier: Modifier,
    active: Boolean,
    onDismiss: () -> Unit,
    choices: List<Choice>,
    onSelect: (Choice) -> Unit,
)

