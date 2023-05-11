package com.micrantha.skouter.ui.navi

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.ui.view.ViewContext

@Composable
fun NavigationAction(
    modifier: Modifier = Modifier,
    viewContext: ViewContext,
    navAction: NavAction
) =
    IconButton(
        modifier = modifier,
        enabled = navAction.enabled,
        onClick = {
            navAction.action(viewContext)
        }
    ) {
        Icon(
            imageVector = navAction.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
