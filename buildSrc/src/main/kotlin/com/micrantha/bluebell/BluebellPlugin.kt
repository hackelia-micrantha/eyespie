package com.micrantha.bluebell

import org.gradle.api.Plugin
import org.gradle.api.Project

open class BluebellPlugin : Plugin<Project> {
    private var ext: BluebellExtension? = null

    override fun apply(project: Project) = project.run {
        val bluebell = bluebellExtension().also { ext = it }

        configurePlugins()

        afterEvaluate {
            configureBuilds(bluebell.config)
            configureModels(bluebell.models)
            configureGraphql(bluebell.graphql, bluebell.config)
        }
    }
}

fun Project.bluebellExtension(): BluebellExtension = extensions.create(
    "bluebell", BluebellExtension::class.java
)

private fun Project.configurePlugins() {
    plugins.apply("com.github.gmazzo.buildconfig")
}

private fun Project.configureGraphql(graphql: GraphqlConfig, config: BluebellConfig) {

    if (graphql.endpoint.isNullOrBlank()) {
        config.properties["SUPABASE_URL"]?.let {
            graphql.endpoint = "$it/graphql/v1"
        }
    }

    config.properties["SUPABASE_KEY"]?.let { key ->
        graphql.headers = graphql.headers.toMutableMap().apply {
            put("apikey", key)
        }
    }
}