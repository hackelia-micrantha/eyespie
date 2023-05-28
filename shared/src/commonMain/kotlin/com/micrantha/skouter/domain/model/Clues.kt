package com.micrantha.skouter.domain.model

sealed interface Clue<T : Comparable<T>> : Comparable<Clue<T>> {
    val data: T
    val points: Int

    fun display() = data.toString()

    override fun compareTo(other: Clue<T>) = when (val res = points.compareTo(other.points)) {
        0 -> data.compareTo(other.data)
        else -> res
    }
}

data class Clues(
    val label: LabelClue? = null
)

data class ColorClue(
    override val data: String,
    override val points: Int = 1
) : Clue<String>

data class LabelClue(
    override val data: String,
    val confidence: Float
) : Clue<String> {

    override val points: Int = (10 - (10 * confidence)).toInt()

    override fun compareTo(other: Clue<String>): Int {
        return if (other is LabelClue) {
            confidence.compareTo(other.confidence)
        } else {
            data.compareTo(other.data)
        }
    }
}

data class LocationClue(
    override val data: Location,
    override val points: Int
) : Clue<Location>

data class RhymeClue(
    override val data: String,
    override val points: Int = 1
) : Clue<String>
