package com.micrantha.bluebell.platform

actual object AppConfigDelegate : PlatformConfigDelegate, AppConfigRequireDelegate {
    private var delegate: PlatformConfigDelegate? = null

    // Careful with race conditions, load first
    fun load(delegate: PlatformConfigDelegate) {
        this.delegate = delegate
    }

    actual override fun getConfigValue(key: String, defaultValue: String): String =
        this.delegate?.getConfigValue(key, defaultValue) ?: defaultValue

    actual override fun requireConfigValue(key: String): String =
        this.delegate!!.requireConfigValue(key)
}
