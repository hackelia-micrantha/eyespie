package com.micrantha.bluebell

import org.gradle.api.Plugin
import org.gradle.api.Project

open class BluebellPlugin : Plugin<Project> {
    override fun apply(project: Project) = project.run {
        val bluebell = bluebellExtension()

        configurePlugins()

        afterEvaluate {
            configureBuilds(bluebell.config)
            downloadModels(bluebell.models)
        }
    }
}

fun Project.bluebellExtension(): BluebellExtension = extensions.create(
    "bluebell", BluebellExtension::class.java
)

private fun Project.configurePlugins() {
    plugins.apply("com.github.gmazzo.buildconfig")
}
