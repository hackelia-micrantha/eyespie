package com.micrantha.bluebell

import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.gradle.api.Project
import java.util.Properties
import java.io.FileInputStream

private fun loadConfigFromEnvironment(config: BluebellConfig): Properties {
    val properties = Properties()
    FileInputStream(config.envFile).use { fileInputStream ->
        properties.load(fileInputStream)
    }
    return properties
}

fun Project.configureBuilds(config: BluebellConfig) {

    extensions.configure(BuildConfigExtension::class.java) {
        packageName(config.packageName)
        useKotlinOutput { topLevelConstants = true }

        println("> Generating ${config.packageName}")

        val task = tasks.register("generateBluebellConfig") {
            group = "Bluebell"
            description = "Generates the variables for build config"

            val isDebug = tasks.any { it.name.contains("debug", ignoreCase = true) }

            if (config.debugOnly && !isDebug) {
                println("> Ignoring non debug build config ($name).")
                return@register
            }

            if (config.skip) {
                println("> Skipping build config.")
                return@register
            }

            val properties by lazy {
                loadConfigFromEnvironment(config)
            }

            println("> Loaded ${properties.keys.size} variables from ${config.envFile}")

            if (properties.count { config.requiredKeys.contains(it.key) } != config.requiredKeys.size) {
                println("e: a ${config.envFile} file must contain the following variables:")
                config.requiredKeys.forEach { println("  - $it") }
                error("missing configuration")
            }

            config.keys.forEach { if (!properties.containsKey(it)) properties[it] = "" }

            properties.stringPropertyNames().forEach { key ->
                buildConfigField("String", key, "\"${properties[key] ?: ""}\"")
            }
        }

        generateTask.get().dependsOn(task)
    }
}
