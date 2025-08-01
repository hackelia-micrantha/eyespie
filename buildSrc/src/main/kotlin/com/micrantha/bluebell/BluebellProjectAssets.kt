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

internal fun Project.downloadAsset(fileName: String, url: String, tempDir: File): Result<File> {
    try {
        if (!tempDir.exists()) {
            tempDir.mkdirs()
        }
        val destination = tempDir.resolve(fileName)

        if (destination.exists()) {
            logger.lifecycle("> Download $fileName already exists, skipping")
            return Result.success(destination)
        }

        logger.lifecycle("> Downloading $fileName from $url")
        URI(url).toURL().openStream().use { input ->
            destination.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return Result.success(destination)
    } catch (err: Throwable) {
        logger.error("Unable to download $fileName from $url", err)
        return Result.failure(err)
    }
}

internal fun Project.downloadAssets(assets: BluebellAssets) {

    val tempDir by lazy { layout.buildDirectory.dir("tmp").get().asFile }

    val tempAssets = assets.downloads.fold(mutableListOf<BluebellDownload<File>>()) { results, file ->

        file.url?.let { url ->
            downloadAsset(file.name, url, tempDir).map {
                BluebellDownload(
                    ios = it,
                    android = it,
                    name = file.name
                )
            }.onSuccess(results::add)

            return@fold results
        }

        file.iosUrl?.let { url ->
            downloadAsset(file.name, url, tempDir).map {
                    BluebellDownload(
                        ios = it,
                        android = null,
                        name = file.name
                    )
            }.onSuccess(results::add)
        }

        file.androidUrl?.let { url ->
            downloadAsset(file.name, url, tempDir).map {
                BluebellDownload(
                    ios = null,
                    android = it,
                    name = file.name
                )
            }.onSuccess(results::add)
        }

        results
    }

    for (file in tempAssets) {

        val iosOutput by lazy {
            projectDir.resolve(assets.iosDestination).resolve(file.name)
        }
        val androidOutput by lazy {
            projectDir.resolve(assets.androidDestination).resolve(file.name)
        }

        file.ios?.copyTo(iosOutput, overwrite = true)
        file.android?.copyTo(androidOutput, overwrite = true)

        if (file.ios != null && file.android == null) {
            logger.lifecycle("> ${file.name} added to ios resources")
        } else if (file.android != null && file.ios == null) {
            logger.lifecycle("> ${file.name} added to android resources")
        } else {
            logger.lifecycle("> ${file.name} added to ios and android resources")
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
        val to = File(projectDir.path, file.destination)

        from.copyTo(to, true)
    }
}
