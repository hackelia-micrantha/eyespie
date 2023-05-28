package com.micrantha.skouter.ui.scan.preview

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.model.CameraImage
import com.micrantha.skouter.domain.model.Clues
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.LocationClue

data class ScanState(
    val labels: LabelProof? = null,
    val location: LocationClue? = null,
    val image: CameraImage? = null
)

data class ScanUiState(
    val clues: Clues
)

sealed class ScanAction : Action {
    object SaveScan : ScanAction()

    data class ImageCaptured(val image: CameraImage) : ScanAction()

    data class LabelScanned(val data: LabelProof) : ScanAction()
}
