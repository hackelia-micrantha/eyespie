package com.micrantha.bluebell.ui.screen

import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer

interface ScreenEnvironment<State> : Reducer<State>, Effect<State>
