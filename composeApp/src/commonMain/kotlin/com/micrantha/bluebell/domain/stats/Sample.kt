package com.micrantha.bluebell.domain.stats

import kotlin.random.Random

fun <T> Collection<T>.weightedRandomSample(block: (T) -> Double): T? {
    if (isEmpty()) return null

    val weights = this.map(block)

    val totalWeight = weights.sum()

    val randomNumber = Random.nextDouble() * totalWeight

    var cumulativeWeight = 0.0
    for (i in indices) {
        cumulativeWeight += weights[i]
        if (randomNumber < cumulativeWeight) {
            return elementAt(i)
        }
    }

    return null
}
