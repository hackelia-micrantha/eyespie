package com.micrantha.eyespie.features.scan.data.mapping

import com.micrantha.bluebell.app.fail
import com.micrantha.eyespie.features.scan.data.model.ImageResponse
import com.micrantha.eyespie.features.scan.data.model.LabelClueData
import com.micrantha.eyespie.features.scan.data.model.LocationClueData
import com.micrantha.eyespie.features.scan.data.model.RemoteImageLabel
import com.micrantha.eyespie.domain.entities.Clue
import com.micrantha.eyespie.domain.entities.LabelClue
import com.micrantha.eyespie.domain.entities.LabelProof
import com.micrantha.eyespie.domain.entities.Location
import com.micrantha.eyespie.domain.entities.LocationClue
import kotlinx.collections.immutable.toPersistentSet

class ClueDomainMapper {

    fun label(data: RemoteImageLabel) = LabelClue(data.text, data.confidence)

    fun response(data: ImageResponse): LabelProof = data.map(::label).toPersistentSet()

    fun clue(data: Location.Data) = LocationClue(
        data = data,
    )

    fun location(data: Location.Data) = LocationClueData(
        name = data.name,
        city = data.city,
        region = data.region,
        country = data.country,
        accuracy = if (data.accuracy.isNaN().not()) data.accuracy else null
    )

    fun clue(data: LocationClueData) = clue(
        Location.Data(
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
                name = this.name,
                city = this.city,
                region = this.region,
                country = this.country,
                accuracy = this.accuracy
            )
        }

        else -> fail("unknown clue type $data")
    }

}

