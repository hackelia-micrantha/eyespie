package com.micrantha.bluebell.ui.screen

import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer

interface ScreenEnvironment<State> : Reducer<State>, Effect<State>

interface MappedScreenEnvironment<State, UiState> : ScreenEnvironment<State>,
    StateMapper<State, UiState>
