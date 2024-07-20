package com.micrantha.eyespie.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.eyespie.ui.component.Choice

@Composable
actual fun ChoiceSelector(
    modifier: Modifier,
    active: Boolean,
    onDismiss: () -> Unit,
    choices: List<Choice>,
    onSelect: (Choice) -> Unit,
) = Unit
