package com.micrantha.bluebell.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.ui.theme.Dimensions

// TODO: style
@Composable
fun LabeledText(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.Start
) {

    Text(
        text = label,
        style = MaterialTheme.typography.bodySmall
    )
    Text(
        modifier = Modifier.padding(start = Dimensions.content),
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )
}
