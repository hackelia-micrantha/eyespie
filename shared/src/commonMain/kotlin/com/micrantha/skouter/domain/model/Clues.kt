package com.micrantha.skouter.domain.model

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap

sealed interface Clue<T> {
    val data: T

    fun display() = data.toString()
}

sealed interface SortedClue<T : Comparable<T>> : Clue<T>, Comparable<SortedClue<T>> {
    override fun compareTo(other: SortedClue<T>) = data.compareTo(other.data)
}

data class Clues(
    val label: LabelClue? = null,
    val location: LocationClue? = null,
    val color: ColorClue? = null,
    val detect: DetectClue? = null,
    val segment: SegmentClue? = null,
)

data class ColorClue(
    override val data: String,
    private val rgb: Int,
) : Clue<String> {
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
    override val data: Location, // TODO: make a geofence area
) : SortedClue<Location> {
    override fun compareTo(other: SortedClue<Location>): Int {
        return if (other is LocationClue) {
            other.data.accuracy.compareTo(data.accuracy)
        } else {
            data.compareTo(other.data)
        }
    }
}

data class RhymeClue(
    override val data: String,
) : Clue<String>

data class DetectClue(
    val rect: Rect,
    override val data: Int,
    val labels: LabelProof
) : Clue<Int> {

    override fun hashCode() = data.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other is DetectClue) {
            return data == other.data
        }
        return super.equals(other)
    }
}

data class SegmentClue(
    val data: ImageBitmap,
)
