package com.micrantha.skouter.domain.logic

import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.Location
import kotlin.math.max

class GameLogic {

    fun calculatePoints(location: Location): Int {
        return max(1, (location.accuracy * 10).toInt())
    }

    fun calculatePoints(label: LabelClue): Int {
        return (10 - (10 * label.confidence)).toInt()
    }
}
