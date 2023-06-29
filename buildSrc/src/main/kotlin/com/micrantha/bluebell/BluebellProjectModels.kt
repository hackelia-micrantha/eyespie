package com.micrantha.bluebell

import org.gradle.api.Project
import java.io.File

fun Project.downloadModels(models: BluebellModels) {

    val task = tasks.register("downloadModels") {
        group = "Bluebell"
        description = "Downloads machine learning models"


        val outputDirectory = File(models.outputPath)
        outputDirectory.mkdirs()

        execute("git", "lfs", "install")

        for (id in models.ids) {
            println("Fetching model: $id")
            System.out.flush()
            execute(arrayOf("git", "clone", "https://huggingface.co/$id"), outputDirectory)
        }
        println("${models.ids.size} machine learning models retrieved.")

        for (entry in models.files) {
            println("Creating ${entry.value}")
            val from = File(entry.key)
            val to = File(entry.value)
            from.renameTo(to)
        }
    }

    tasks.findByName("generateBluebellConfig")?.dependsOn(task)
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
