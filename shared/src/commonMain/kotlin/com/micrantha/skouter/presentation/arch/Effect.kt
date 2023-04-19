package com.micrantha.bluebell.ui.arch


typealias Effect<State> = suspend (Action, State) -> Unit
