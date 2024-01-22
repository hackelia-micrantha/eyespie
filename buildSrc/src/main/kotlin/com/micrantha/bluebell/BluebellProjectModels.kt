package com.micrantha.bluebell

import org.gradle.api.Project
import java.io.File

fun Project.configureModels(models: BluebellModels) {

    val task = tasks.register("configureModels") {
        group = "Bluebell"
        description = "Configure machine learning models"

        downloadModels(models)

        if (models.outputPaths.isNotEmpty()) {
            copyModels(models)
        }
    }

    tasks.findByName("generateBluebellConfig")?.dependsOn(task)
}

internal fun downloadModels(models: BluebellModels) {
    /*execute("git", "lfs", "install")

    for (id in models.ids) {
        println("Fetching model: $id")
        System.out.flush()
        execute(arrayOf("git", "clone", "https://huggingface.co/$id"), outputDirectory)
    }
    println("${models.ids.size} machine learning models retrieved.")
     */
}

internal fun copyModels(models: BluebellModels) {

    println("> Model output paths:")

    for (outputPath in models.outputPaths) {
        println("  - ${outputPath}")
        File(outputPath).mkdirs()
    }

    for (entry in models.files) {
        val from = File(models.downloadPath, entry.key)

        if (from.exists().not()) {
            println("> Model ${from.path} does not exist, skipping")
            continue
        }
        println("> Copying ${entry.key} to ${entry.value}")

        for (outputPath in models.outputPaths) {
            val to = File(outputPath, entry.value)
            from.copyTo(to, true)
        }
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
