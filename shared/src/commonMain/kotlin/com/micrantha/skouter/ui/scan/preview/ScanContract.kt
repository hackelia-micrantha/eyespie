package com.micrantha.skouter.ui.scan.preview

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.model.Clues
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.LocationClue
import com.micrantha.skouter.domain.model.MatchClue
import com.micrantha.skouter.domain.model.MatchProof
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.domain.model.SegmentClue
import com.micrantha.skouter.domain.model.SegmentProof
import com.micrantha.skouter.platform.CameraImage
import okio.Path
import kotlin.math.max

data class ScanState(
    val labels: LabelProof? = null,
    val location: Location? = null,
    val colors: ColorProof? = null,
    val image: CameraImage? = null,
    val enabled: Boolean = true,
    val detection: DetectClue? = null,
    val segment: SegmentClue? = null,
    val match: MatchClue? = null,
    val path: Path? = null,
    val playerID: String? = null
)

data class ScanUiState(
    val clues: List<String>,
    val overlays: List<ScanOverlay>,
    val enabled: Boolean
)

sealed class ScanAction : Action {
    interface ScanSavable {
        val path: Path
    }

    object SaveScan : ScanAction()

    object EditScan : ScanAction()

    object SaveError : ScanAction()

    data class EditSaved(override val path: Path) : ScanAction(), ScanSavable
    data class ImageSaved(override val path: Path) : ScanAction(), ScanSavable

    data class ImageCaptured(val image: CameraImage) : ScanAction()

    data class ScannedLabels(
        val labels: LabelProof
    ) : ScanAction()

    data class ScannedColors(
        val colors: ColorProof
    ) : ScanAction()

    data class ScannedObjects(
        val detections: DetectProof
    ) : ScanAction()

    data class ScannedSegments(
        val segments: SegmentProof
    ) : ScanAction()

    data class ScannedMatch(
        val matches: MatchProof
    ) : ScanAction()
}

sealed interface ScanOverlay

data class ScanMask(
    val mask: ImageBitmap,
) : ScanOverlay

data class ScanBox(
    val rect: Rect,
    val label: String,
    val scale: Float
) : ScanOverlay


internal fun ScanState.asProof() = Proof(
    clues = Clues(
        labels = labels,
        location = LocationClue(location!!.data!!),
        colors = colors
    ),
    name = "",
    image = path!!,
    match = match!!.data,
    location = location,
    playerID = playerID!!
)

internal fun ScanState.clues() = mutableListOf<String>().apply {
    labels?.firstOrNull {
        add("What: ${it.display()} +${labels.size - 1}")
    }
    colors?.firstOrNull { add("Color: ${it.display()} +${colors.size - 1}") }
    location?.let { add("Location: ${it.point}") }
}

internal fun ScanState.overlays(): List<ScanOverlay> {
    val result = mutableListOf<ScanOverlay>()
    if (image == null) return result
    detection?.let {
        result.add(it.asScanBoxIn(image.width, image.height))
    }
    segment?.let {
        result.add(ScanMask(it.data))
    }
    return result
}

internal fun DetectClue.asScanBoxIn(width: Int, height: Int) = ScanBox(
    rect,
    labels.firstOrNull()?.display() ?: "",
    max(rect.width / width, rect.height / height)
)
