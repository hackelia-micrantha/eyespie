package com.micrantha.bluebell.platform

import kotlin.reflect.KProperty

expect object AppConfigDelegate : AppConfigPropertyDelegate {
    override fun getConfigValue(key: String, defaultValue: String): String
    override fun requireConfigValue(key: String): String
}

class OptionalAppConfigDelegate(private val defaultValue: String = "") {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        AppConfigDelegate.getConfigValue(property.name, defaultValue)
}

interface PlatformConfigDelegate {
    fun getConfigValue(key: String, defaultValue: String = ""): String
    fun requireConfigValue(key: String): String
}

interface AppConfigPropertyDelegate : PlatformConfigDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        requireConfigValue(property.name)
}
