package com.micrantha.bluebell.platform

actual object AppConfigDelegate : AppConfigPropertyDelegate {
    private lateinit var delegate: PlatformConfigDelegate

    // Careful with race conditions, load first
    fun load(delegate: PlatformConfigDelegate) {
        this.delegate = delegate
    }

    actual override fun getConfigValue(key: String): String = this.delegate.getConfigValue(key)
}
