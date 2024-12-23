package com.micrantha.bluebell

import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.gradle.api.Project
import java.util.Properties
import java.io.FileInputStream

private fun loadConfigFromEnvironment(config: BluebellConfig): Properties {
    val properties = Properties()
    try {
        FileInputStream(config.envFile).use { fileInputStream ->
            properties.load(fileInputStream)
        }
    } catch (e: Exception) {
        println("> No ${config.envFile} file found.")
    }
    return properties
}

fun Project.configureBuilds(config: BluebellConfig) {

    extensions.configure(BuildConfigExtension::class.java) {
        packageName(config.packageName)
        useKotlinOutput { topLevelConstants = true }

        println("> Generating ${config.packageName}")

        val requiredConfigError = { ->
            println("e: a ${config.envFile} file must contain the following variables:")
            config.requiredKeys.forEach { println("  - $it") }
            error("missing configuration")
        }

        val setDefaultConfig = { ->
            if (config.requiredKeys.isNotEmpty()) {
                requiredConfigError()
            }
            config.keys.forEach {
                buildConfigField("String", it, "\"\"")
            }
        }

        val task = tasks.register("generateBluebellConfig") {
            group = "Bluebell"
            description = "Generates the variables for build config"

            val isDebug = tasks.any { it.name.contains("debug", ignoreCase = true) }

            if (config.debugOnly && !isDebug) {
                println("> Ignoring non debug build config ($name).")
                setDefaultConfig()
                return@register
            }

            if (config.skip) {
                println("> Skipping build config.")
                setDefaultConfig()
                return@register
            }

            val properties by lazy {
                loadConfigFromEnvironment(config)
            }

            println("> Loaded ${properties.keys.size} variables from ${config.envFile}")

            if (properties.count { config.requiredKeys.contains(it.key) } != config.requiredKeys.size) {
                requiredConfigError()
            }

            config.keys.forEach { if (!properties.containsKey(it)) properties[it] = "" }

            properties.stringPropertyNames().forEach { key ->
                buildConfigField("String", key, "\"${properties[key] ?: ""}\"")
            }
        }

        generateTask.get().dependsOn(task)
    }
}
