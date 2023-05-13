package com.micrantha.bluebell.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.ui.theme.Dimensions

@Composable
@Deprecated(
    "not appropriate layout for varying screen dimensions",
    replaceWith = ReplaceWith("LabeledText")
)
fun HorizontalLabeledText(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {

    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        modifier = Modifier.padding(start = Dimensions.content),
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )

}
