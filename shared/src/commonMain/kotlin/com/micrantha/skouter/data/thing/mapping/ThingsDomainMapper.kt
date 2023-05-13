package com.micrantha.skouter.data.thing.mapping

import com.micrantha.skouter.data.thing.model.RecognitionResponse
import com.micrantha.skouter.domain.models.Clues

class ThingsDomainMapper {

    operator fun invoke(data: RecognitionResponse): Clues {
        val label = data.labels.maxByOrNull { it.probability }
        return Clues(what = label?.label)
    }
}
