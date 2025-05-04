package com.micrantha.bluebell

import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import com.github.gmazzo.gradle.plugins.BuildConfigTask
import org.gradle.api.Project
import java.io.FileInputStream
import java.util.Properties

fun Project.configureBuilds(config: BluebellConfig) {

    fun loadConfigFromEnvironment(config: BluebellConfig): Properties {
        val properties = Properties()
        try {
            FileInputStream(config.envFile).use { fileInputStream ->
                properties.load(fileInputStream)
            }
            logger.info("Loaded ${properties.keys.size} variables from ${config.envFile}")
        } catch (e: Exception) {
            logger.warn("No ${config.envFile} file found.")
        }
        return properties
    }

    val envConfig by lazy {
        loadConfigFromEnvironment(config).entries.associate { (key, value) -> key.toString() to "\"$value\"" }
            .toMutableMap().apply {
                config.defaultKeys.filterNot { containsKey(it) }.forEach {
                    this[it] = "null"
                }
            }
    }

    val requiredConfigError = { ->
        logger.error("${config.envFile} must contain the following variables:")
        config.requiredKeys.forEach { logger.error("  - $it") }
        error("missing configuration")
    }

    fun generateSource(task: BuildConfigTask) {
        val entries =
            envConfig.entries.map { "\"${it.key}\" to ${config.className}.${it.key}" }

        val outputDir =
            task.outputDir.dir(config.packageName.replace(".", "/")).get().also {
                it.asFile.mkdirs()
            }

        val sourceFile = outputDir.file("${config.className}Ext.kt").asFile

        // Example code generation logic
        sourceFile.writeText(generatedExtensionSourceCode(config, entries))
        logger.info("Generated config extensions")
    }

    extensions.configure(BuildConfigExtension::class.java) {
        packageName(config.packageName)
        className(config.className)
        useKotlinOutput { topLevelConstants = false }

        fun configureBuild() {
            if (envConfig.count { config.requiredKeys.contains(it.key) } != config.requiredKeys.size) {
                requiredConfigError()
            }

            envConfig.forEach { (key, value) ->
                buildConfigField("String?", key, value)
            }

            logger.info("Generated ${config.packageName}.${config.className}")
        }

        tasks.register("generateBluebellConfig") {
            group = "Bluebell"
            description = "Generates the local build config"

            configureBuild()
        }

        tasks.register("generateBluebellConfigExtensions") {
            group = "Bluebell"
            description = "Generates the local build config extensions"

            generateSource(generateTask.get())
        }

        generateTask.configure {
            doFirst {
                configureBuild()
            }
            doLast {
                generateSource(this as BuildConfigTask)
            }
        }
    }

}

private fun generatedExtensionSourceCode(config: BluebellConfig, entries: List<String>) = """
package ${config.packageName}

private val map = mapOf(
    ${entries.joinToString(",\n    ")}
)

fun ${config.className}.get(key: String): String? {
    return map[key]
}    
""".trimIndent()
