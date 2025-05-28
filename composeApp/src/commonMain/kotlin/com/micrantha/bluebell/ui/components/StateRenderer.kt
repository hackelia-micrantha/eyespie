package com.micrantha.bluebell.ui.components

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.arch.Dispatch

interface StateRenderer<State> {
    @Composable
    fun Render(state: State, dispatch: Dispatch)
}
