package com.micrantha.bluebell.platform

import com.micrantha.mobuildenvuscator.MobuildEnvuscator
import kotlin.jvm.Throws

class ConfigError(message: String) : Throwable(message)

actual object AppConfigDelegate : AppConfigPropertyDelegate {
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
