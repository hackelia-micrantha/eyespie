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
import com.micrantha.skouter.domain.model.MatchProof
import com.micrantha.skouter.domain.model.SegmentClue
import com.micrantha.skouter.domain.model.SegmentProof
import com.micrantha.skouter.platform.scan.model.ScanColor
import com.micrantha.skouter.platform.scan.model.ScanEmbedding
import com.micrantha.skouter.platform.scan.model.ScanLabel
import com.micrantha.skouter.platform.scan.model.ScanLabels
import com.micrantha.skouter.platform.scan.model.ScanObject
import com.micrantha.skouter.platform.scan.model.ScanSegment
import com.micrantha.skouter.platform.scan.model.label
import com.micrantha.skouter.platform.scan.model.labels
import com.micrantha.skouter.platform.scan.model.rect
import com.micrantha.skouter.platform.scan.model.score
import com.micrantha.skouter.platform.scan.toImageBitmap

class ClueDomainMapper {

    @Deprecated("dependency on backend is not wanted")
    fun recognition(data: RecognitionResponse): LabelProof =
        data.labels.map { LabelClue(it.label, it.probability) }.toSet()

    fun label(data: ScanLabel) = LabelClue(data.label, data.score)

    fun labels(data: ScanLabels): LabelProof = data.map(::label).toSet()

    fun detect(data: ScanObject) = DetectClue(
        data = data.rect,
        labels = data.labels.map { LabelClue(it.label, it.score) }.toSet(),
    )

    fun detect(data: List<ScanObject>): DetectProof = data.map(::detect).toSet()

    fun segment(data: ScanSegment) = SegmentClue(
        data = data.toImageBitmap()
    )

    fun segment(data: List<ScanSegment>): SegmentProof = data.map(::segment)

    fun match(data: ScanEmbedding) = MatchProof(
        data = data
    )

    fun color(data: ScanColor) = ColorClue(
        data = data.name,
    )

    fun color(data: List<ScanColor>): ColorProof = data.map(::color).toSet()

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

