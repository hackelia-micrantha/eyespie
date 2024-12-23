package com.micrantha.bluebell.platform

import kotlin.reflect.KProperty
import com.micrantha.eyespie.config.DefaultConfig
import com.micrantha.eyespie.config.get

expect object AppConfigDelegate : AppConfigPropertyDelegate {
    override fun getConfigValue(key: String, defaultValue: String): String
    override fun requireConfigValue(key: String): String
}

object AppConfigDelegateWithDefault {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        AppConfigDelegate.getConfigValue(property.name, DefaultConfig.get(property.name) ?: "")
}

interface PlatformConfigDelegate {
    fun getConfigValue(key: String, defaultValue: String = ""): String
    fun requireConfigValue(key: String): String
}

interface AppConfigPropertyDelegate : PlatformConfigDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        requireConfigValue(property.name)
}
