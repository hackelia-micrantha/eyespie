package com.micrantha.bluebell

import org.gradle.api.Project
import java.io.File

fun Project.configureModels(models: BluebellModels) {

    val task = tasks.register("configureModels") {
        group = "Bluebell"
        description = "Configure machine learning models"

        if (models.outputPaths.isNotEmpty()) {
            copyModels(models)
        }
    }

    tasks.findByName("generateBluebellConfig")?.dependsOn(task)
}

internal fun Project.copyModels(models: BluebellModels) {

    logger.debug("Model output paths:")

    for (outputPath in models.outputPaths) {
        logger.debug("  - $outputPath")
        File(outputPath).mkdirs()
    }

    for (entry in models.files) {
        val from = File(models.downloadPath, entry.key)

        if (from.exists().not()) {
            logger.warn("Model ${from.path} does not exist, skipping")
            continue
        }
        logger.info("Copying ${entry.key} to ${entry.value}")

        for (outputPath in models.outputPaths) {
            val to = File(outputPath, entry.value)
            from.copyTo(to, true)
        }
    }
}
