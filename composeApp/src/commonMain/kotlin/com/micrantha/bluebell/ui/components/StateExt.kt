package com.micrantha.bluebell.ui.components

import com.micrantha.bluebell.app.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

fun <State, UiState> StateFlow<State>.mapIn(
    scope: CoroutineScope,
    mapper: (State) -> UiState
): StateFlow<UiState> =
    map(mapper).catch { Log.e("state", it) { "unable to map state" } }
        .stateIn(scope, SharingStarted.Eagerly, mapper(value))
