package com.micrantha.bluebell

import org.gradle.api.Project
import java.io.File
import java.net.URI

fun Project.configureAssets(assets: BluebellAssets) {

    val task = tasks.register("configureAssets") {
        group = "Bluebell"
        description = "Configure assets"

        copyAssets(assets)
        downloadAssets(assets)
    }

    tasks.findByName("generateBluebellConfig")?.dependsOn(task)
}

internal fun Project.downloadAssets(assets: BluebellAssets) {

    for (file in assets.downloads) {
        val destination = File(projectDir.path, file.destination)

        val parent = destination.parentFile
        if (parent.exists().not() && parent.mkdirs().not()) {
            logger.warn("Unable to create $destination for ${file.name}, skipping")
            continue
        }

        logger.lifecycle("Downloading from ${file.url} to ${file.destination}")
        URI(file.url).toURL().openStream().use { input ->
            destination.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}

internal fun Project.copyAssets(assets: BluebellAssets) {

    for (file in assets.copies) {
        val from = File(projectDir.path, file.source)

        if (from.exists().not()) {
            logger.warn("Asset ${from.path} does not exist, skipping")
            continue
        }

        logger.lifecycle("Copying ${file.id} from ${from.path} to ${file.destination}")

        val to = File(projectDir.path, file.destination)
        from.copyTo(to, true)
    }
}
