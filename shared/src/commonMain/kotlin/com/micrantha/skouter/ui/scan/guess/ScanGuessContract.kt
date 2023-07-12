package com.micrantha.skouter.ui.scan.guess

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.platform.CameraImage
import kotlinx.serialization.Serializable

data class ScanGuessState(
    val thing: Thing? = null,
)

data class ScanGuessUiState(
    val guessed: Boolean,
    val enabled: Boolean
)

@Serializable
data class ScanGuessArgs(
    val id: String
)

sealed interface ScanGuessAction : Action {
    data class ImageCaptured(val image: CameraImage) : ScanGuessAction
    data object ThingMatched : ScanGuessAction
    data object ThingNotFound : ScanGuessAction

    data object Load : ScanGuessAction
    data class Loaded(val thing: Thing) : ScanGuessAction
}
