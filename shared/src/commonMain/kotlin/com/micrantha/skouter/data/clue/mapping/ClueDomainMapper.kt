package com.micrantha.skouter.data.clue.mapping

import com.micrantha.bluebell.data.fail
import com.micrantha.skouter.data.clue.model.LabelClueData
import com.micrantha.skouter.data.clue.model.LocationClueData
import com.micrantha.skouter.data.clue.model.RecognitionResponse
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.domain.model.ColorClue
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.LocationClue
import com.micrantha.skouter.domain.model.MatchClue
import com.micrantha.skouter.domain.model.MatchProof
import com.micrantha.skouter.domain.model.SegmentClue
import com.micrantha.skouter.domain.model.SegmentProof
import com.micrantha.skouter.platform.ImageColor
import com.micrantha.skouter.platform.ImageEmbedding
import com.micrantha.skouter.platform.ImageLabel
import com.micrantha.skouter.platform.ImageObject
import com.micrantha.skouter.platform.ImageSegment

class ClueDomainMapper {

    @Deprecated("dependency on backend is not wanted")
    fun recognition(data: RecognitionResponse): LabelProof =
        data.labels.map { LabelClue(it.label, it.probability) }.toSet()

    fun label(data: ImageLabel) = LabelClue(data.data, data.confidence)

    fun label(data: List<ImageLabel>): LabelProof = data.map(::label).toSet()

    fun detect(data: ImageObject) = DetectClue(
        data = data.rect,
        labels = data.labels.map { LabelClue(it.data, it.confidence) }.toSet(),
    )

    fun detect(data: List<ImageObject>): DetectProof = data.map(::detect).toSet()

    fun segment(data: ImageSegment) = SegmentClue(
        data = data.mask.toImageBitmap()
    )

    fun segment(data: List<ImageSegment>): SegmentProof = data.map(::segment)

    fun match(data: ImageEmbedding) = MatchClue(
        data = data
    )

    fun match(data: List<ImageEmbedding>): MatchProof = data.map(::match)

    fun color(data: ImageColor) = ColorClue(
        data = data.name,
    )

    fun color(data: List<ImageColor>): ColorProof = data.map(::color).toSet()

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

