package com.micrantha.bluebell

import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.gradle.api.Project
import java.io.File
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
        className(config.className)
        useKotlinOutput { topLevelConstants = false }

        println("> Generating ${config.packageName}.${config.className}")

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

        val properties by lazy {
            loadConfigFromEnvironment(config)
        }

        val configureBuild = tasks.register("generateBluebellConfig") {
            group = "Bluebell"
            description = "Generates the variables for build config"

            val isDebug = tasks.any { it.name.contains("debug", ignoreCase = true) }

            if (config.debugValuesOnly && !isDebug) {
                println("> Ignoring non debug build config.")
                setDefaultConfig()
                return@register
            }

            if (config.skip) {
                println("> Skipping build config.")
                setDefaultConfig()
                return@register
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

        val generateExtensions = tasks.register("generateBluebellConfigExtensions") {

            val path = "generated/source/buildConfig/main/main/${config.packageName.replace('.','/')}"

            println("> Generating ${config.className} extensions")

            val outputDir = project.layout.buildDirectory.dir(path).get()

            // Example code generation logic
            val generatedFile = outputDir.file("${config.className}Ext.kt").asFile
            generatedFile.writeText(
                """
package ${config.packageName}

private val map = mapOf(
    ${properties.map { "\"${it.key}\" to \"${it.value}\"" }.joinToString(",\n\t")}
)

fun ${config.className}.get(key: String): String? {
    return map[key]
}
            """
            )
        }

        configureBuild.get().dependsOn(generateExtensions)

        generateTask.get().dependsOn(configureBuild)

    }
}
