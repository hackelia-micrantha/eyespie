package com.micrantha.skouter.domain.model

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import com.micrantha.skouter.platform.scan.model.ScanEmbedding
import okio.Path

data class Clues(
    val labels: Set<LabelClue>? = null,
    val location: LocationClue? = null, // TODO: Geofence
    val colors: Set<ColorClue>? = null
)

data class Proof(
    val clues: Clues?,
    val location: Location.Point?,
    val match: ScanEmbedding,
    val image: Path,
    val name: String?,
    val playerID: String
)

typealias LabelProof = Set<LabelClue>

typealias ColorProof = Set<ColorClue>

typealias LocationProof = LocationClue

typealias DetectProof = Set<DetectClue>

typealias SegmentProof = List<SegmentClue>

typealias MatchProof = MatchClue

sealed interface Clue<T> {
    val data: T

    fun display() = data.toString()
}

sealed interface SortedClue<T : Comparable<T>> : Clue<T>, Comparable<SortedClue<T>> {
    override fun compareTo(other: SortedClue<T>) = data.compareTo(other.data)
}

data class ColorClue(
    override val data: String,
) : SortedClue<String> {
    override fun hashCode() = data.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other is ColorClue) {
            return other.data == data
        }
        return super.equals(other)
    }
}

data class LabelClue(
    override val data: String,
    val confidence: Float
) : SortedClue<String> {

    override fun compareTo(other: SortedClue<String>): Int {
        return if (other is LabelClue) {
            other.confidence.compareTo(confidence)
        } else {
            data.compareTo(other.data)
        }
    }

    override fun hashCode() = data.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other is LabelClue) {
            return data == other.data
        }
        return super.equals(other)
    }
}

data class LocationClue(
    override val data: Location.Data, // TODO: make a geofence area
) : SortedClue<Location.Data>

data class RhymeClue(
    override val data: String,
) : Clue<String>

data class DetectClue(
    override val data: Rect,
    val labels: LabelProof
) : Clue<Rect>

data class SegmentClue(
    override val data: ImageBitmap,
) : Clue<ImageBitmap>

data class MatchClue(
    override val data: ScanEmbedding
) : Clue<ScanEmbedding> {
    override fun equals(other: Any?): Boolean {
        if (other !is MatchClue) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}
