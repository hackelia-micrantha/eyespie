package com.micrantha.skouter.ui.navi

import androidx.compose.ui.graphics.vector.ImageVector
import com.micrantha.bluebell.ui.screen.ScreenContext

typealias ActionHandler = (ScreenContext) -> Unit

data class NavAction(
    val title: String? = null,
    val icon: ImageVector,
    val action: ActionHandler,
    val enabled: Boolean = true
) {
    fun toggle(value: Boolean) = copy(enabled = value)
}
