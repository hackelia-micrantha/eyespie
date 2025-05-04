package com.micrantha.bluebell.app

import com.micrantha.bluebell.app.navi.NavAction

interface Scaffolding {

    fun title(): String? = null

    fun actions(): List<NavAction>? = null

    fun backAction(): NavAction? = null

    val showBack: Boolean
        get() = true

    val canGoBack: Boolean?
        get() = null
}
