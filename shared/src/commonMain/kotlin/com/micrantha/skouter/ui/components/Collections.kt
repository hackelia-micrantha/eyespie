package com.micrantha.skouter.ui.components

fun <T> List<T>.mapIf(condition: (T) -> Boolean, transform: (T) -> T) = map {
    if (condition(it)) transform(it) else it
}
