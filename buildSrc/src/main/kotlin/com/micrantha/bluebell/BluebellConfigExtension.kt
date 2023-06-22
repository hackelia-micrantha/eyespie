package com.micrantha.bluebell

import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import java.io.File
import java.util.Properties
import javax.inject.Inject

open class BluebellConfigExtension @Inject constructor(
    objects: ObjectFactory
) {
    val config: BluebellConfig = objects.newInstance(BluebellConfig::class.java)

    fun config(action: Action<BluebellConfig>) {
        action.execute(config)
    }
}

open class BluebellConfig {
    var fileName: String = "config.properties"
    var packageName: String = "com.micrantha.bluebell"
    var className: String = "BuildConfig"

    override fun toString() = "$fileName ($packageName.$className)"
}

fun Project.bluebellConfig(): BluebellConfigExtension = extensions.create(
    "bluebell", BluebellConfigExtension::class.java
)

private fun Project.loadProperties(config: BluebellConfig): Properties {
    val properties = Properties()
    try {
        properties.load(this.rootProject.file(config.fileName).reader())
    } catch (err: Throwable) {
        error("Could not load $config")
    }
    return properties
}

fun Project.configureBuilds(config: BluebellConfig) =
    this.extensions.configure(BuildConfigExtension::class.java) {
        packageName.set(config.packageName)

        className.set(config.className)

        val properties by lazy { loadProperties(config) }

        properties.stringPropertyNames().forEach { key ->
            buildConfigField("String", key, "\"${properties[key] ?: ""}\"")
        }
    }
