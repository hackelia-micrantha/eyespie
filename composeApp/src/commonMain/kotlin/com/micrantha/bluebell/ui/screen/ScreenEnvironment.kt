package com.micrantha.bluebell.ui.screen

import com.micrantha.bluebell.arch.Effect
import com.micrantha.bluebell.arch.Reducer

interface ScreenEnvironment<State> : Reducer<State>, Effect<State>
