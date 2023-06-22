package com.micrantha.bluebell

import org.gradle.api.Plugin
import org.gradle.api.Project

open class BluebellPlugin : Plugin<Project> {
    override fun apply(project: Project) = project.run {
        val buildConfig = bluebellConfig()

        configurePlugins()

        afterEvaluate {
            configureBuilds(buildConfig.config)
        }
    }
}

private fun Project.configurePlugins() {
    plugins.apply("com.github.gmazzo.buildconfig")
}
