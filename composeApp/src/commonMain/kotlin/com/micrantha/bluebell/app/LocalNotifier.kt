package com.micrantha.bluebell.app

interface LocalNotifier {
    fun schedule(
        id: String,
        title: String,
        message: String,
        atMillis: Long? = null
    )

    fun cancel(id: String)
}
