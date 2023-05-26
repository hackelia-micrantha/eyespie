package com.micrantha.bluebell.ui.scaffold

import androidx.compose.runtime.Composable
import com.micrantha.skouter.ui.navi.NavAction

interface Scaffolding {

    @Composable
    fun title(): String? = null

    fun actions(): List<NavAction>? = null

    fun backAction(): NavAction? = null

    fun showBack(): Boolean = true

}
