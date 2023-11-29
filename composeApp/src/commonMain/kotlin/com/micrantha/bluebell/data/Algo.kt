package com.micrantha.bluebell.data

import io.ktor.utils.io.core.toByteArray
import okio.Buffer
import okio.HashingSink.Companion.sha256
import okio.use
import kotlin.random.Random

fun hash(input: String): String = Buffer().use {
    it.write(input.toByteArray()).flush()
    sha256(it).hash.base64Url()
}

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
