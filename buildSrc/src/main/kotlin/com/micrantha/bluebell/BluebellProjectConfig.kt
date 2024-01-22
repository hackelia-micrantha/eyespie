package com.micrantha.bluebell

import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.gradle.api.Project
import java.io.File
import java.util.Properties

private fun Project.loadProperties(config: BluebellConfig): Properties {
    val properties = Properties()
    try {
        properties.load(this.rootProject.file(config.fileName).reader())
        println("> ${properties.size} properties loaded from ${config.fileName}")
    } catch (err: Throwable) {
        File(config.fileName).createNewFile()
        println("> Created properties file ${config.fileName}")
    }
    return properties
}

fun Project.configureBuilds(config: BluebellConfig) {

    extensions.configure(BuildConfigExtension::class.java) {
        packageName(config.packageName)
        className(config.className)

        val task = tasks.register("generateBluebellConfig") {
            group = "Bluebell"
            description = "Generates the variables for build config"

            val properties by lazy { loadProperties(config) }

            if (properties.isEmpty) {
                println("e: please configure a properties file (${config.fileName}) containing configuration for:")
                println("  supaBaseUrl, supaBaseKey, huggingFaceApiToken, userLoginEmail, userLoginPassword")
                error("no build configuration")
            }

            properties.stringPropertyNames().forEach { key ->
                buildConfigField("String", key, "\"${properties[key] ?: ""}\"")
            }
        }

        generateTask.get().dependsOn(task)
    }
}
