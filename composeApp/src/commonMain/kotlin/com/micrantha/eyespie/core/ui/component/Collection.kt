package com.micrantha.eyespie.core.ui.component

fun <T> List<T>.mapIf(condition: (T) -> Boolean, transform: (T) -> T) = map {
    if (condition(it)) transform(it) else it
}

fun <K, V> MutableMap<K, V>.updateKey(key: K, transform: (V) -> V): MutableMap<K, V> {
    this[key]?.let {
        this[key] = transform(it)
    }
    return this
}


fun <T> Set<T>?.combine(other: Set<T>): Set<T> {
    if (this == null) return other
    return this.plus(other)
}

fun <T> Set<T>?.combine(other: T): Set<T> {
    if (this == null) return setOf(other)
    return this.plus(other)
}

