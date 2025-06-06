package com.micrantha.eyespie.domain.entities

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import okio.ByteString
import okio.Path

data class Clues(
    val labels: LabelProof? = null,
    val location: LocationClue? = null, // TODO: Geofence
    val colors: ColorProof? = null
)

data class Proof(
    val clues: Clues?,
    val location: Location.Point?,
    val match: Embedding,
    val image: Path,
    val name: String?,
    val playerID: String
)

typealias LabelProof = Set<LabelClue>

typealias ColorProof = Set<ColorClue>

typealias LocationProof = LocationClue

typealias DetectProof = Set<DetectClue>

typealias SegmentProof = List<SegmentClue>

typealias MatchProof = List<MatchClue>

sealed interface Clue<T> {
    val data: T

    fun display() = data.toString()
}

sealed interface SortedClue<T : Comparable<T>> : Clue<T>, Comparable<SortedClue<T>> {
    override fun compareTo(other: SortedClue<T>) = data.compareTo(other.data)
}

data class ColorClue(
    override val data: String
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
    override val data: ImageBitmap
) : Clue<ImageBitmap>

typealias Embedding = ByteString

data class MatchClue(
    override val data: Embedding
) : Clue<Embedding>
