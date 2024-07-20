package com.micrantha.eyespie.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.ui.EyesPie

@Composable
fun AppTitle() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(
                end = Dimensions.content
            ).size(
                MaterialTheme.typography.headlineLarge.fontSize.value.dp
            ),
            imageVector = EyesPie.defaultIcon,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
        )
        Text(
            text = stringResource(S.AppTitle),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
