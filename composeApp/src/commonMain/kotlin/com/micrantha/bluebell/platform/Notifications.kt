package com.micrantha.bluebell.platform

import com.micrantha.bluebell.app.LocalNotifier

expect class Notifications : LocalNotifier {
    override fun schedule(
        id: String,
        title: String,
        message: String,
        atMillis: Long?
    )

    override fun cancel(id: String)
}
