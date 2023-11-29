package com.micrantha.bluebell.platform

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.domain.model.UiMessage

@Composable
expect fun DisplayMessage(message: UiMessage)
