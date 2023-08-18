package com.micrantha.bluebell.ui.components

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.domain.arch.Dispatch

interface StateRenderer<State> {
    @Composable
    fun Render(state: State, dispatch: Dispatch)
}
