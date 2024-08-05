package com.micrantha.bluebell.platform

import com.micrantha.mobuildenvuscator.MobuildEnvuscator
import kotlin.reflect.KProperty

actual object AppConfigDelegate {
    private val envuscator: MobuildEnvuscator = MobuildEnvuscator()
    actual operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        envuscator.getConfigValue(property.name)
}
