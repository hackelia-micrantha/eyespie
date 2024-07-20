package com.micrantha.eyespie.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.ui.theme.Dimensions

@Composable
fun ListHeader(modifier: Modifier = Modifier, label: String) {
    Text(
        text = label,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary)
            .padding(Dimensions.content),
        style = MaterialTheme.typography.labelLarge
    )
}
