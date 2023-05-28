package com.micrantha.skouter.domain.model

typealias Clues = List<Clue<*>>

interface Clue<T : Comparable<T>> : Comparable<Clue<T>> {
    val data: T

    fun display() = data.toString()
    
    override fun compareTo(other: Clue<T>) = data.compareTo(other.data)
}


typealias Proof<T> = List<Clue<T>>
typealias LabelProof = List<LabelClue>

fun <T : Comparable<T>> Proof<T>.candidate(): Clue<T>? = minOrNull()

data class ColorClue(
    override val data: String,
) : Clue<String>

data class LabelClue(
    override val data: String,
    val confidence: Float
) : Clue<String> {

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
) : Clue<Location>

data class RhymeClue(
    override val data: String,
) : Clue<String>
