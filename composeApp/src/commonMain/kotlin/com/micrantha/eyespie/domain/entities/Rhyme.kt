package com.micrantha.eyespie.domain.entities

data class Rhyme(var word: String, var score: Int, var numSyllables: Int) : Comparable<Rhyme> {
    override fun compareTo(other: Rhyme): Int = other.score.compareTo(score)
}
