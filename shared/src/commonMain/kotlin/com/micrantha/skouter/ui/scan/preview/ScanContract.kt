package com.micrantha.skouter.ui.scan.preview

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.LocationClue
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.platform.CameraImage

data class ScanState(
    val labels: LabelProof? = null,
    val location: LocationClue? = null,
    val colors: ColorProof? = null,
    val objects: DetectProof? = null,
    val image: CameraImage? = null,
    val enabled: Boolean = true,
    val current: DetectClue? = null
)

data class ScanUiState(
    val clues: Set<Clue<*>>,
    val current: Pair<Rect, String>?,
    val imageSize: Size,
    val enabled: Boolean
)

sealed class ScanAction : Action {
    object SaveScan : ScanAction()

    object EditScan : ScanAction()

    object SaveError : ScanAction()

    data class ImageCaptured(val image: CameraImage) : ScanAction()

    data class LabelScanned(val data: LabelProof) : ScanAction()

    data class ColorScanned(val data: ColorProof) : ScanAction()

    data class ObjectScanned(val data: DetectProof) : ScanAction()
}


fun ScanState.asProof() = Proof(
    labels = labels,
    location = location,
    colors = colors,
    objects = objects
)

