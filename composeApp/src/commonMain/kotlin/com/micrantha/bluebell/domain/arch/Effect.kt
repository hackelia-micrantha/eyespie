package com.micrantha.bluebell.domain.arch


typealias Effect<State> = suspend (action: Action, state: State) -> Unit
