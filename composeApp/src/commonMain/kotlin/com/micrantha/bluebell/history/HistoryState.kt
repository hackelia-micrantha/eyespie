package com.micrantha.bluebell.history

import com.micrantha.bluebell.arch.StateMapper

fun <State> historyStateOf(state: State) = HistoryState(state = state)

fun <State, UiState> historyMapperOf(mapper: StateMapper<State, UiState>): StateMapper<HistoryState<State>, UiState> =
    StateMapper { state -> mapper.map(state.state) }

data class HistoryState<State>(
    val prev: List<State> = listOf(),
    val state: State,
    val next: List<State> = listOf()
)

sealed class HistoryAction {
    data object Undo : HistoryAction()
    data object Redo : HistoryAction()
    data class Jump(val index: Int) : HistoryAction()
}

