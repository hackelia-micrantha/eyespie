package com.micrantha.bluebell.platform

import com.micrantha.eyespie.config.DefaultConfig
import com.micrantha.eyespie.config.get
import kotlin.reflect.KProperty

object DefaultAppConfigDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        DefaultConfig.get(property.name) ?: ""
}

object OptionalAppConfigDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        DefaultConfig.get(property.name) ?: ""
}