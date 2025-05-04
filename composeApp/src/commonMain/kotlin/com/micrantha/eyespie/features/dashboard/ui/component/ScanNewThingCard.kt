package com.micrantha.eyespie.features.dashboard.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.micrantha.bluebell.ui.components.stringResource
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.app.S

@Composable
fun ScanNewThingCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(Dimensions.screen).fillMaxWidth().clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimensions.content),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(Dimensions.List.thumbnail),
                imageVector = Icons.Default.AddAPhoto,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )

            Column(
                modifier = Modifier.weight(1f)
                    .padding(horizontal = Dimensions.content),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(bottom = Dimensions.content),
                    text = stringResource(S.NewThing),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = stringResource(S.NewThingDescription),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Icon(
                modifier = Modifier.size(Dimensions.List.thumbnail),
                imageVector = Icons.Default.Add,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
            )
        }
    }
}
