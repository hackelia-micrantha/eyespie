package com.micrantha.skouter.data.clue.mapping

import com.micrantha.bluebell.data.fail
import com.micrantha.skouter.data.clue.model.ImageResponse
import com.micrantha.skouter.data.clue.model.LabelClueData
import com.micrantha.skouter.data.clue.model.LocationClueData
import com.micrantha.skouter.data.clue.model.RemoteImageLabel
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.LocationClue
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

