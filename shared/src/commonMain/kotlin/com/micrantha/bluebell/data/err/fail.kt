package com.micrantha.bluebell.data.err

internal fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}
