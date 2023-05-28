package com.micrantha.skouter.data.clue.mapping

import com.micrantha.bluebell.data.fail
import com.micrantha.skouter.data.clue.model.LabelClueData
import com.micrantha.skouter.data.clue.model.LabelResponse
import com.micrantha.skouter.data.clue.model.RecognitionResponse
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.domain.model.LabelClue

class ClueDomainMapper {

    fun recognition(data: RecognitionResponse) =
        data.labels.map { LabelClue(it.label, it.probability) }

    fun labels(data: List<LabelResponse>) = data.map { LabelClue(it.text, it.confidence) }

    fun clues(data: List<Clue<*>>) = data.map {
        clue(it)
    }

    fun clue(data: Clue<*>) = when (data) {
        is LabelClue -> LabelClueData(data = data.data, confidence = data.confidence)
        else -> fail("unknown clue type $data")
    }
}
