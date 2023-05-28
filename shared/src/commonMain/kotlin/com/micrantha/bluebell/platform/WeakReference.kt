package com.micrantha.bluebell.platform

expect class WeakReference<out T : Any>(target: T) {
    val targetOrNull: T?
}
