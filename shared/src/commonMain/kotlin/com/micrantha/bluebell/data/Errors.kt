package com.micrantha.bluebell.data

internal fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}
