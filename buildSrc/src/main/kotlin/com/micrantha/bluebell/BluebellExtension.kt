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

open class BluebellConfig {
    var fileName: String = "config.properties"
    var packageName: String = "com.micrantha.bluebell"
    var className: String = "BuildConfig"

    override fun toString() = "$fileName ($packageName.$className)"
}

open class BluebellModels {
    var outputPath: String = "models"
    var ids: List<String> = emptyList()
    val files: Map<String, String> = emptyMap()
}

fun Project.bluebellConfig(): BluebellExtension = extensions.create(
    "bluebell", BluebellExtension::class.java
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

fun Project.configureBuilds(config: BluebellConfig) {
    val properties by lazy { loadProperties(config) }

    extensions.configure(BuildConfigExtension::class.java) {
        packageName(config.packageName)
        className(config.className)

        val task = tasks.register("generateBluebellConfig") {
            group = "Bluebell"
            description = "Generates the variables for build config"

            properties.stringPropertyNames().forEach { key ->
                buildConfigField("String", key, "\"${properties[key] ?: ""}\"")
            }
        }

        generateTask.get().dependsOn(task)
    }
}


fun Project.downloadModels(models: BluebellModels) {

    val outputDirectory = File(models.outputPath)
    outputDirectory.mkdirs()

    execute("git", "lfs", "install")

    for (id in models.ids) {
        println("Downloading model: $id")
        execute(arrayOf("git", "clone", "https://huggingface.co/$id"), outputDirectory)
    }
    println("Finished model downloads.")

    for (entry in models.files) {
        println("Creating ${entry.value}")
        val from = File(entry.key)
        val to = File(entry.value)
        from.renameTo(to)
    }
}

private fun execute(vararg command: String) = execute(command = command, outputDirectory = null)

private fun execute(command: Array<out String>, outputDirectory: File? = null) {
    try {
        val process = ProcessBuilder(*command).apply {
            outputDirectory?.let { directory(it) }
        }.start()

        process.waitFor()
    } catch (err: Throwable) {
        err.printStackTrace()
    }
}
