package com.micrantha.bluebell.platform

import kotlin.reflect.KProperty

expect object AppConfigDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String
}
