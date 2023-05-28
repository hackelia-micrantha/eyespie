package com.micrantha.skouter.data.clue.mapping

import com.micrantha.skouter.data.clue.model.LabelResponse
import com.micrantha.skouter.data.clue.model.RecognitionResponse
import com.micrantha.skouter.domain.model.LabelClue

class ClueDomainMapper {

    fun recognition(data: RecognitionResponse) =
        data.labels.map { LabelClue(it.label, it.probability) }

    fun labels(data: List<LabelResponse>) = data.map { LabelClue(it.text, it.confidence) }
}
