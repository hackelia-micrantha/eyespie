package com.micrantha.bluebell

import com.github.gmazzo.buildconfig.BuildConfigExtension
import com.github.gmazzo.buildconfig.BuildConfigTask
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import kotlin.io.path.Path

fun BluebellConfig.loadConfigFromEnvironment(): Result<Map<String, String>> {
    try {
        val properties = Properties()
        FileInputStream(envFile).use { fileInputStream ->
            properties.load(fileInputStream)
        }
        val config = properties.entries.associate { (key, value) -> key.toString() to "\"$value\"" }
            .toMutableMap().apply {
                defaultedKeys.filterNot { containsKey(it) }.forEach {
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

    val requiredKeyError = { key: String ->
        logger.error("> Missing '$key' in ${config.envFile}")
        logger.error("${config.envFile} must contain the following variables:")
        config.requiredKeys.forEach { logger.error("  - $it") }
        error("missing key '$key' in ${config.envFile}")
    }

    fun generateSource(task: BuildConfigTask) {
        val entries =
            config.properties.entries.map { "\"${it.key}\" to ${config.className}.${it.key}" }

        val outputDir = task.outputDir.dir(
            config.packageName.replace(".", File.separator)
        ).get().also {
            it.asFile.mkdirs()
        }

        val sourceFile = outputDir.file("${config.className}Ext.kt").asFile

        // Example code generation logic
        sourceFile.writeText(generatedExtensionSourceCode(config, entries))
        logger.lifecycle("> Generated config extensions")
    }

    extensions.configure(BuildConfigExtension::class.java) {
        packageName(config.packageName)
        className(config.className)
        useKotlinOutput { topLevelConstants = false }

        val configureBuild = {
            config.expectedKeys.forEach { key ->
                if (config.properties.containsKey(key).not()) {
                    logger.warn("> Missing key '$key' in ${config.envFile}")
                }
            }

            config.requiredKeys.forEach { key ->
                if (config.properties.containsKey(key).not()) {
                    requiredKeyError(key)
                }
            }

            config.properties.forEach { (key, value) ->
                buildConfigField("String?", key, value)
            }

            logger.lifecycle("> Generated ${config.packageName}.${config.className}")
        }

        val configTask = tasks.register("generateBluebellConfig") {
            group = "Bluebell"
            description = "Generates the local build config"

            configureBuild()
        }

        generateTask.get().dependsOn(configTask)

        tasks.register("generateBluebellConfigExtensions") {
            group = "Bluebell"
            description = "Generates the local build config extensions"

            dependsOn(configTask)

            generateSource(generateTask.get())
        }
//
//        generateTask.configure {
//            doLast {
//                generateSource(this as BuildConfigTask)
//            }
//        }
    }

}

private fun generatedExtensionSourceCode(config: BluebellConfig, entries: List<String>) = """
package ${config.packageName}
import kotlin.reflect.KProperty

${if(config.properties.isEmpty()) "object ${config.className}" else ""}

private val map = mapOf<String, String?>(
    ${entries.joinToString(",\n    ")}
)

internal fun ${config.className}.get(key: String): String? {
    return map[key]
}    

internal operator fun ${config.className}.getValue(thisRef: Any?, property: KProperty<*>): String =
    map[property.name] ?: ""
""".trimIndent()
