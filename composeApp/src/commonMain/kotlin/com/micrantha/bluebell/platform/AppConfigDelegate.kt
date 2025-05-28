package com.micrantha.bluebell.platform

import com.micrantha.eyespie.config.DefaultConfig
import com.micrantha.eyespie.config.get
import kotlin.reflect.KProperty

expect object AppConfigDelegate : PlatformConfigDelegate, AppConfigRequireDelegate {
    override fun getConfigValue(key: String, defaultValue: String): String
    override fun requireConfigValue(key: String): String
}

object DefaultAppConfigDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        DefaultConfig.get(property.name)
            ?: AppConfigDelegate.requireConfigValue(property.name)
}

object OptionalAppConfigDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        DefaultConfig.get(property.name)
            ?: AppConfigDelegate.getConfigValue(property.name)
}

class UserAppConfigDelegate(private val userValue: String?) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        userValue ?: AppConfigDelegate.getConfigValue(property.name)
}

interface PlatformConfigDelegate {
    fun getConfigValue(key: String, defaultValue: String = ""): String
    fun requireConfigValue(key: String): String
}

interface AppConfigRequireDelegate : PlatformConfigDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        requireConfigValue(property.name)
}