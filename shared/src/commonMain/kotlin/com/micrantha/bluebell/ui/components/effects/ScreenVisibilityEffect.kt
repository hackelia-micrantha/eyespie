package com.micrantha.bluebell.ui.components.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.micrantha.bluebell.ui.components.ScreenVisibility

@Composable
fun ScreenVisibilityEffect(
    callback: ScreenVisibility
) = DisposableEffect(callback) {
    callback.onScreenActive()

    onDispose {
        callback.onScreenIdle()
    }
}
