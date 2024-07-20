package com.micrantha.eyespie.ui.navi

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.ui.screen.LocalScreenContext

@Composable
fun NavigationAction(
    modifier: Modifier = Modifier,
    navAction: NavAction
) {
    val context = LocalScreenContext.current

    IconButton(
        modifier = modifier,
        enabled = navAction.enabled,
        onClick = {
            navAction.action(context)
        }
    ) {
        Icon(
            imageVector = navAction.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
