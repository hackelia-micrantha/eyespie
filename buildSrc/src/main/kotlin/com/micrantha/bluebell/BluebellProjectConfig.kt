package com.micrantha.bluebell

import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.gradle.api.Project
import java.io.File
import java.util.Properties
import io.github.cdimascio.dotenv.dotenv

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

            val properties by lazy {
                loadConfigFromEnvironment(config)
            }

            val requiredProperties = listOf(
                "SUPABASE_URL",
                "SUPABASE_KEY",
                "HUGGINGFACE_TOKEN"
            )

            val optionalProperties = listOf(
                "LOGIN_EMAIL",
                "LOGIN_PASSWORD"
            )

            if (properties.count { requiredProperties.contains(it.key) } != requiredProperties.size) {
                println("e: a .env file must contain the following variables:")
                requiredProperties.forEach { println("  - ${config.envVarName}_$it") }
                error("missing configuration")
            }

            optionalProperties.forEach { if (!properties.containsKey(it)) properties[it] = "" }

            properties.stringPropertyNames().forEach { key ->
                buildConfigField("String", key, "\"${properties[key] ?: ""}\"")
            }
        }

        generateTask.get().dependsOn(task)
    }
}
