package com.micrantha.skouter.ui.scan.capture

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.MatchClue
import com.micrantha.skouter.domain.model.SegmentClue
import com.micrantha.skouter.platform.scan.CameraImage
import kotlinx.coroutines.flow.Flow
import okio.Path
import kotlin.math.min

@Stable
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
    val playerID: String? = null,
)

@Stable
data class ScanUiState(
    val clues: List<String>,
    val overlays: List<ScanOverlay>,
    val enabled: Boolean,
    val capture: Painter?
)

sealed interface ScanAction {
    interface ScanSavable {
        val path: Path
    }

    data object SaveScan : ScanAction

    data object EditScan : ScanAction

    data object SaveError : ScanAction

    data object LoadError : ScanAction

    data object ScanError : ScanAction

    data class Loaded(val data: Flow<CameraImage>) : ScanAction

    data class EditSaved(override val path: Path) : ScanAction, ScanSavable
    data class ImageSaved(override val path: Path) : ScanAction, ScanSavable

    data object Back : ScanAction
}

sealed interface ScanOverlay

data class ScanMask(
    val mask: ImageBitmap,
) : ScanOverlay

data class ScanBox(
    val rect: Rect,
    val label: String,
    val imageWidth: Int,
    val imageHeight: Int
) : ScanOverlay {

    fun scale(width: Float, height: Float): Rect {
        val scale = min(width * 1f / imageWidth, height * 1f / imageHeight)
        return with(rect) {
            Rect(
                left = left * scale,
                top = top * scale,
                right = right * scale,
                bottom = bottom * scale
            )
        }
    }
}
