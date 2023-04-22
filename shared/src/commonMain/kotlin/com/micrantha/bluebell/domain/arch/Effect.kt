package com.micrantha.bluebell.domain.arch


typealias Effect<State> = suspend (Action, State) -> Unit
