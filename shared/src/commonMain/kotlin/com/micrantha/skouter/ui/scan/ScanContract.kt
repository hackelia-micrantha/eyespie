package com.micrantha.skouter.ui.scan

import androidx.compose.ui.graphics.painter.Painter
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.model.Clues

data class ScanState(
    val status: UiResult<Unit> = UiResult.Default,
    val clues: Clues? = null,
    val image: Painter? = null
)

data class ScanUiState(
    val status: UiResult<Data> = UiResult.Default
) {
    data class Data(
        val clues: Clues?,
        val image: Painter
    )
}

sealed class ScanAction : Action {
    object Init : ScanAction()

    object SaveScan : ScanAction()

    data class ImageCaptured(val data: ByteArray) : ScanAction()

    data class NoCamera(val err: Throwable) : ScanAction()

    data class ScanError(val err: Throwable) : ScanAction()

    data class ScannedClues(val data: Clues) : ScanAction()

    data class NoClues(val data: ByteArray) : ScanAction()
}
