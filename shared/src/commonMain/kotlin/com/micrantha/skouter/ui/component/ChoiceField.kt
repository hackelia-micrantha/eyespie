package com.micrantha.skouter.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.platform.ChoiceSelector

data class Choice(
    val label: String,
    val tag: String,
    val key: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceField(
    modifier: Modifier = Modifier,
    choices: List<Choice>,
    onValue: (Choice) -> String = { it.label },
    label: @Composable (Choice) -> Unit,
    trailingIcon: @Composable () -> Unit,
    onCustom: ((String) -> Unit)? = null,
    onSelect: (Choice) -> Unit,
) {
    var current by remember { mutableStateOf(choices.first()) }
    var active by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = onValue(current),
            readOnly = onCustom == null,
            onValueChange = onCustom ?: { },
            label = { label(current) },
            trailingIcon = {
                Row {
                    Icon(
                        modifier = Modifier.padding(end = Dimensions.content)
                            .clickable {
                                active = active.not()
                                if (active.not()) {
                                    onSelect(current)
                                }
                            },
                        imageVector = if (active)
                            Icons.Default.ArrowDropUp
                        else
                            Icons.Default.ArrowDropDown,
                        contentDescription = null,
                    )
                    trailingIcon()
                }
            }
        )

        ChoiceSelector(
            modifier = Modifier.align(Alignment.BottomEnd),
            active = active,
            onDismiss = {
                active = false
                onSelect(current)
            },
            choices = choices,
            onSelect = { choice ->
                active = false
                current = choice
            },
        )
    }
}
