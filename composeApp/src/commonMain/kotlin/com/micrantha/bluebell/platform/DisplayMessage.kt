package com.micrantha.bluebell.platform

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.ui.model.UiMessage

@Composable
expect fun DisplayMessage(message: UiMessage)
