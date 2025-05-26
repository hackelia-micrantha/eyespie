package com.micrantha.eyespie.features.dashboard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.app.S
import eyespie.composeapp.generated.resources.has_more
import org.jetbrains.compose.resources.stringResource

@Composable
fun HasMoreFooter(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = Dimensions.content),
                text = stringResource(S.has_more)
            )
        }
    }
}
