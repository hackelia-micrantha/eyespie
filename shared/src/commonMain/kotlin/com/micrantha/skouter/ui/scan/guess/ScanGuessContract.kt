package com.micrantha.skouter.ui.scan.guess

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.platform.CameraImage

data class ScanGuessState(
    val guessed: Boolean = false
)

data class ScanGuessUiState(
    val guessed: Boolean,
    val enabled: Boolean
)

data class ScanGuessArgs(
    val id: String
)

sealed class ScanGuessAction : Action {
    data class ImageCaptured(val image: CameraImage) : ScanGuessAction()
    object ThingMatched : ScanGuessAction()
    object ThingNotFound : ScanGuessAction()
}
