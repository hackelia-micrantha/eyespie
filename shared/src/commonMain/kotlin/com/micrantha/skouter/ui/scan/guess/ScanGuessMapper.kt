package com.micrantha.skouter.ui.scan.guess

class ScanGuessMapper {

    operator fun invoke(state: ScanGuessState) = ScanGuessUiState(
        guessed = state.guessed,
        enabled = true
    )
}
