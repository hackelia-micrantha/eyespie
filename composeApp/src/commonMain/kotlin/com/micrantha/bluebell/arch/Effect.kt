package com.micrantha.bluebell.arch


typealias Effect<State> = suspend (action: Action, state: State) -> Unit
