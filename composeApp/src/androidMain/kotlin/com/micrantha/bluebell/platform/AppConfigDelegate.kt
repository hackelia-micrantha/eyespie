package com.micrantha.bluebell.platform

import com.micrantha.mobuildenvuscator.MobuildEnvuscator

actual object AppConfigDelegate : AppConfigPropertyDelegate {
    private val envuscator: MobuildEnvuscator = MobuildEnvuscator()
    actual override fun getConfigValue(key: String): String =
        envuscator.getConfigValue(key)
}
