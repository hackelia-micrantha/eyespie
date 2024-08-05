package com.micrantha.bluebell.platform

import MobuildEnvuscator.mobuild_envuscator_get_config_value
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toCValues
import platform.posix.SIZE_MAX
import kotlin.reflect.KProperty

actual object AppConfigDelegate {
    @OptIn(ExperimentalForeignApi::class)
    actual operator fun getValue(thisRef: Any?, property: KProperty<*>): String = memScoped {
        val keyBytes = property.name.encodeToByteArray()
        val keyPtr = keyBytes.toCValues().ptr
        val resultBytes = ByteArray(256) // Adjust size as needed
        val resultPtr = resultBytes.toCValues().ptr

        // Call the C function
        val resultSize = mobuild_envuscator_get_config_value(
            keyPtr.reinterpret(),
            keyBytes.size.toULong(),
            resultPtr.reinterpret(),
            resultBytes.size.toULong()
        )

        // Convert result to Kotlin String
        if (resultSize == SIZE_MAX) {
            throw NoSuchElementException("key not found in envuscator config")
        }
        return resultBytes.decodeToString(0, resultSize.toInt())
    }
}
