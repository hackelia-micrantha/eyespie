package com.micrantha.eyespie.features.guess.ui

class ScanGuessMapper {

    operator fun invoke(state: ScanGuessState) = ScanGuessUiState(
        guessed = state.thing?.guessed == true,
        enabled = state.thing != null
    )
}
