package com.micrantha.eyespie.ui.scan.guess

class ScanGuessMapper {

    operator fun invoke(state: ScanGuessState) = ScanGuessUiState(
        guessed = state.thing?.guessed ?: false,
        enabled = state.thing != null
    )
}
