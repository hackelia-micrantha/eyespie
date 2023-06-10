package com.micrantha.skouter.data.clue.mapping

import com.micrantha.bluebell.data.fail
import com.micrantha.skouter.data.clue.model.LabelClueData
import com.micrantha.skouter.data.clue.model.LabelResponse
import com.micrantha.skouter.data.clue.model.LocationClueData
import com.micrantha.skouter.data.clue.model.RecognitionResponse
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.LocationClue
import kotlin.math.max

class ClueDomainMapper {

    fun recognition(data: RecognitionResponse) =
        data.labels.map { LabelClue(it.label, it.probability) }

    fun labels(data: List<LabelResponse>) = data.map { LabelClue(it.text, it.confidence) }

    fun clue(data: Location) = LocationClue(
        data = data,
        points = data.calculatePoints()
    )

    fun location(data: Location) = LocationClueData(
        name = data.name,
        city = data.city,
        region = data.region,
        country = data.country,
        accuracy = if (data.accuracy.isNaN().not()) data.accuracy else null
    )

    fun clue(data: LocationClueData) = clue(
        Location(
            name = data.name,
            city = data.city,
            region = data.region,
            country = data.country,
            accuracy = data.accuracy ?: Float.NaN
        )
    )

    fun clues(data: List<Clue<*>>) = data.map {
        clue(it)
    }

    fun clue(data: Clue<*>) = when (data) {
        is LabelClue -> LabelClueData(data = data.data, confidence = data.confidence)
        is LocationClue -> with(data.data) {
            LocationClueData(
                name = name,
                city = city,
                region = region,
                country = country,
                accuracy = accuracy
            )
        }
        else -> fail("unknown clue type $data")
    }

    private fun Location.calculatePoints(): Int {
        return max(1, 10 - accuracy.toInt())
    }
}

