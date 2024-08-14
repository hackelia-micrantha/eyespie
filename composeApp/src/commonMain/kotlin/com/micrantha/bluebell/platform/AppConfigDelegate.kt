package com.micrantha.bluebell.platform

import kotlin.reflect.KProperty

expect object AppConfigDelegate : AppConfigPropertyDelegate {
    override fun getConfigValue(key: String): String
}

interface PlatformConfigDelegate {
    fun getConfigValue(key: String): String
}

interface AppConfigPropertyDelegate : PlatformConfigDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        getConfigValue(property.name)
}
