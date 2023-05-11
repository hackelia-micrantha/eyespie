package com.micrantha.bluebell.ui.components

interface ScreenVisibility {

    fun onScreenActive() = Unit

    fun onScreenIdle() = Unit
}
