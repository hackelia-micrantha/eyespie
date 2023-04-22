package com.micrantha.bluebell.ui.view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

fun <State, UiState> StateFlow<State>.mapIn(
    scope: CoroutineScope,
    mapper: (State) -> UiState
): StateFlow<UiState> =
    map(mapper).stateIn(scope, SharingStarted.Eagerly, mapper(value))
