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

data class WeightedItem<T>(val item: T, val weight: Int)

infix fun <T> T.weightedTo(that: Int): WeightedItem<T> = WeightedItem(this, that)

fun <T, D> Collection<T>.weightedRandomSample(block: (T) -> WeightedItem<D>): T? {
    val cumulativeSum = this.map(block).scan(0) { sum, item -> sum + item.weight }

    val totalWeight = cumulativeSum.lastOrNull() ?: return null
    val randomNumber = Random.nextInt(totalWeight)

    for (i in cumulativeSum.indices) {
        if (randomNumber < cumulativeSum[i]) {
            return this.elementAt(i)
        }
    }

    return null
}
