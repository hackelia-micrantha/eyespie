package com.micrantha.eyespie.ui.scan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.ui.theme.Dimensions


@Composable
fun ScannedClues(modifier: Modifier, clues: Collection<String>) {
    Column(
        modifier = modifier
            .padding(Dimensions.screen)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(0.5f),
                shape = RoundedCornerShape(Dimensions.content)
            )
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
