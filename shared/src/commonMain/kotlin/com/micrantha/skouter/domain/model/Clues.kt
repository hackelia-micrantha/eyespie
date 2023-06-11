package com.micrantha.skouter.domain.model

import com.micrantha.skouter.domain.model.DetectClue.Box

sealed interface Clue<T : Comparable<T>> : Comparable<Clue<T>> {
    val data: T

    fun display() = data.toString()

    override fun compareTo(other: Clue<T>) = data.compareTo(other.data)
}

data class Clues(
    val label: LabelClue? = null,
    val location: LocationClue? = null,
    val color: ColorClue? = null,
    val detect: DetectClue? = null,
)

data class ColorClue(
    override val data: String,
    private val rgb: Int,
) : Clue<String> {
    override fun hashCode() = data.hashCode()

    override fun equals(other: Any?) = data == other
}

data class LabelClue(
    override val data: String,
    val confidence: Float
) : Clue<String> {

    override fun compareTo(other: Clue<String>): Int {
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
) : Clue<Location> {
    override fun compareTo(other: Clue<Location>): Int {
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
    override val data: Box,
    val id: Int,
    val labels: LabelProof
) : Clue<Box> {
    data class Box(
        val x: Float, val y: Float, val w: Float, val h: Float
    ) : Comparable<Box> {

        val area = w * h

        override fun compareTo(other: Box) = other.area.compareTo(area)

        override fun hashCode(): Int = (x + y + w + h).hashCode()

        override fun equals(other: Any?): Boolean {
            if (other is Box) {
                return x == other.x && y == other.y && w == other.w && h == other.h
            }
            return super.equals(other)
        }
    }

    override fun compareTo(other: Clue<Box>): Int {
        if (other is DetectClue) {
            return other.id.compareTo(id)
        }
        return super.compareTo(other)
    }

    override fun hashCode() = data.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other is DetectClue) {
            if (id == -1 || other.id == -1) {
                return data == other.data
            }
            return id == other.id
        }
        return super.equals(other)
    }
}
