package com.micrantha.bluebell

import com.github.gmazzo.buildconfig.BuildConfigExtension
import com.github.gmazzo.buildconfig.BuildConfigTask
import org.gradle.api.Project
import java.io.FileInputStream
import java.util.Properties

fun BluebellConfig.loadConfigFromEnvironment(): Result<Map<String, String>> {
    try {
        val properties = Properties()
        FileInputStream(envFile).use { fileInputStream ->
            properties.load(fileInputStream)
        }
        val config = properties.entries.associate { (key, value) -> key.toString() to "\"$value\"" }
            .toMutableMap().apply {
                defaultKeys.filterNot { containsKey(it) }.forEach {
                    this[it] = "null"
                }
            }
        return Result.success(config.toMap())
    } catch (e: Exception) {
        return Result.failure(e)
    }
}

fun Project.configureBuilds(config: BluebellConfig) {

    config.properties = config.loadConfigFromEnvironment().getOrDefault(emptyMap())

    val requiredConfigError = { ->
        logger.error("${config.envFile} must contain the following variables:")
        config.requiredKeys.forEach { logger.error("  - $it") }
        error("missing configuration")
    }

    fun generateSource(task: BuildConfigTask) {
        val entries =
            config.properties.entries.map { "\"${it.key}\" to ${config.className}.${it.key}" }

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
            if (config.properties.count { config.requiredKeys.contains(it.key) } != config.requiredKeys.size) {
                requiredConfigError()
            }

            config.properties.forEach { (key, value) ->
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
            doLast {
                generateSource(this as BuildConfigTask)
            }
        }
    }

}

private fun generatedExtensionSourceCode(config: BluebellConfig, entries: List<String>) = """
package ${config.packageName}

${if(config.properties.isEmpty()) "object DefaultConfig" else ""}

private val map = mapOf<String, String?>(
    ${entries.joinToString(",\n    ")}
)

internal fun ${config.className}.get(key: String): String? {
    return map[key]
}    
""".trimIndent()
