package com.micrantha.bluebell

import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import io.github.cdimascio.dotenv.dotenv
import org.gradle.api.Project
import java.util.Properties

private fun loadConfigFromEnvironment(config: BluebellConfig): Properties {
    val properties = Properties()
    val prefix = "${config.envVarName}_"

    dotenv {
        ignoreIfMissing = true
    }.entries().filter { it.key.startsWith(prefix) }.forEach { e ->
        properties[e.key.removePrefix(prefix)] = e.value
    }
    return properties
}

fun Project.configureBuilds(config: BluebellConfig) {

    extensions.configure(BuildConfigExtension::class.java) {
        packageName(config.packageName)
        className(config.className)

        val task = tasks.register("generateBluebellConfig") {
            group = "Bluebell"
            description = "Generates the variables for build config"

            if (config.skip) {
                return@register
            }

            val properties by lazy {
                loadConfigFromEnvironment(config)
            }

            if (properties.count { config.requiredKeys.contains(it.key) } != config.requiredKeys.size) {
                println("e: a .env file must contain the following variables:")
                config.requiredKeys.forEach { println("  - ${config.envVarName}_$it") }
                error("missing configuration")
            }

            config.nonRequiredKeys.forEach { if (!properties.containsKey(it)) properties[it] = "" }

            properties.stringPropertyNames().forEach { key ->
                buildConfigField("String", key, "\"${properties[key] ?: ""}\"")
            }
        }

        generateTask.get().dependsOn(task)
    }
}
