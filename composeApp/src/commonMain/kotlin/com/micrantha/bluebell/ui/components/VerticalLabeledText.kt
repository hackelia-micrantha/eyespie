package com.micrantha.bluebell.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import com.micrantha.bluebell.ui.theme.Dimensions

@Composable
fun VerticalLabeledText(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    labelAlignmentLine: VerticalAlignmentLine
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    Text(
        modifier = Modifier.alignBy(labelAlignmentLine),
        text = label,
        style = MaterialTheme.typography.labelSmall
    )
    Text(
        modifier = Modifier.padding(start = Dimensions.content),
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )
}
