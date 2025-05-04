package com.micrantha.bluebell.app

internal fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}
