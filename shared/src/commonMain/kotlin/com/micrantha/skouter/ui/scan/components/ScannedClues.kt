package com.micrantha.skouter.ui.scan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.ui.theme.Dimensions


@Composable
fun ScannedClues(modifier: Modifier, clues: List<String>) {
    Surface(
        modifier = modifier.padding(Dimensions.screen)
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            clues.forEach {
                Text(
                    text = it,
                    maxLines = 1,
                    modifier = Modifier.padding(Dimensions.content)
                )
            }
        }
    }
}
