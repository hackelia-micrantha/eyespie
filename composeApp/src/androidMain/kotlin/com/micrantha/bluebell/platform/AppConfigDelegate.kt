package com.micrantha.bluebell.platform

import com.micrantha.mobuildenvuscator.MobuildEnvuscator

class ConfigError(message: String) : Throwable(message)

actual object AppConfigDelegate : PlatformConfigDelegate, AppConfigRequireDelegate {
    private val envuscator: MobuildEnvuscator = MobuildEnvuscator()

    @Throws(ConfigError::class)
    actual override fun requireConfigValue(key: String): String = try {
        envuscator.get(key)
    } catch (e: Throwable) {
        throw ConfigError(e.toString())
    }

    actual override fun getConfigValue(key: String, defaultValue: String): String = try {
        envuscator.get(key)
    } catch (e: Throwable) {
        defaultValue
    }
}
