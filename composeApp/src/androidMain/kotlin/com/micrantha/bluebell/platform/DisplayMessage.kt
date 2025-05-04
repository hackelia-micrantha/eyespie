package com.micrantha.bluebell.platform

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.micrantha.bluebell.ui.model.UiMessage
import com.micrantha.bluebell.ui.model.UiMessage.Type.Popup
import com.micrantha.bluebell.ui.components.message.icon

@Composable
actual fun DisplayMessage(message: UiMessage) = when (val msg = message.type) {
    is Popup -> msg.Display(message)
    else -> Unit
}

@Composable
private fun Popup.Display(message: UiMessage) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            positive?.let {
                Button(onClick = it.onClick) {
                    Text(it.label)
                }
            }
        },
        text = { Text(message.message) },
        icon = {
            message.category.icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
        },
        title = { title?.let { Text(it) } }
    )
}
