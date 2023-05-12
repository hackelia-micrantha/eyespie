package com.micrantha.bluebell.ui.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import com.micrantha.bluebell.ui.scaffold.ScaffoldAction.SetNavigation
import com.micrantha.skouter.ui.navi.NavAction

class ScaffoldBuilder {
    var title: String? = null
    var showBack: Boolean? = null
    var onBack: NavAction? = null
    var actions: MutableList<NavAction>? = null

    fun action(value: NavAction) {
        if (actions != null) {
            actions?.add(value)
        } else {
            actions = mutableListOf(value)
        }
    }

    fun build(): SetNavigation {

        if (showBack == true && onBack == null) {
            onBack = NavAction(
                icon = Icons.Default.KeyboardArrowLeft,
                action = { context -> context.navigateBack() }
            )
        }

        return SetNavigation(this)
    }
}
