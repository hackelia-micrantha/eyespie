package com.micrantha.bluebell.ui.err
internal fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}
