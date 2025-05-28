package com.micrantha.eyespie.domain.logic

import com.micrantha.eyespie.domain.entities.LabelClue
import com.micrantha.eyespie.domain.entities.Location
import kotlin.math.max

class GameLogic {

    fun calculatePoints(location: Location.Data): Int {
        return max(1, (location.accuracy * 10).toInt())
    }

    fun calculatePoints(label: LabelClue): Int {
        return (10 - (10 * label.confidence)).toInt()
    }
}
