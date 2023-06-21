package com.micrantha.skouter.ui.scan.preview

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.model.ColorClue
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.LocationClue
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.domain.model.SegmentClue
import com.micrantha.skouter.platform.CameraImage
import kotlin.math.max

data class ScanState(
    val labels: LabelProof? = null,
    val location: LocationClue? = null,
    val colors: ColorProof? = null,
    val detections: DetectProof? = null,
    val image: CameraImage? = null,
    val enabled: Boolean = true,
    val currentThing: DetectClue? = null,
    val currentSegment: SegmentClue? = null,
)

data class ScanUiState(
    val clues: List<String>,
    val overlays: List<ScanOverlay>,
    val enabled: Boolean
)

sealed class ScanAction : Action {
    object SaveScan : ScanAction()

    object EditScan : ScanAction()

    object SaveError : ScanAction()

    data class ImageScanned(
        val labels: LabelClue,
        val colors: ColorClue,
        val detections: DetectClue,
        val segments: SegmentClue
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
    labels = labels,
    location = location,
    colors = colors
)

internal fun ScanState.clues() = mutableListOf<String>().apply {
    labels?.firstOrNull {
        add("What: ${it.display()} +${labels.size - 1}")
    }
    colors?.firstOrNull { add("Color: ${it.display()} +${colors.size - 1}") }
    location?.let { add("Location: ${it.display()}") }
}

internal fun ScanState.overlays(): List<ScanOverlay> {
    val result = mutableListOf<ScanOverlay>()
    if (image == null) return result
    currentThing?.let {
        result.add(it.asScanBoxIn(image.width, image.height))
    }
    currentSegment?.let {
        result.add(ScanMask(it.data))
    }
    return result
}

internal fun DetectClue.asScanBoxIn(width: Int, height: Int) = ScanBox(
    rect,
    labels.firstOrNull()?.display() ?: "",
    max(rect.width / width, rect.height / height)
)
