package com.micrantha.bluebell

import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import java.io.File
import java.util.Properties
import javax.inject.Inject

open class BluebellExtension @Inject constructor(
    objects: ObjectFactory
) {
    val config: BluebellConfig = objects.newInstance(BluebellConfig::class.java)

    val models: BluebellModels = objects.newInstance(BluebellModels::class.java)

    fun config(action: Action<BluebellConfig>) {
        action.execute(config)
    }

    fun models(action: Action<BluebellModels>) {
        action.execute(models)
    }
}
