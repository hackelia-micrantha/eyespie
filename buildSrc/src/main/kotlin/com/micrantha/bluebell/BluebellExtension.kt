package com.micrantha.bluebell

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

open class BluebellExtension @Inject constructor(
    objects: ObjectFactory
) {
    val config: BluebellConfig = objects.newInstance(BluebellConfig::class.java)

    val models: BluebellModels = objects.newInstance(BluebellModels::class.java)

    val graphql: GraphqlConfig = objects.newInstance(GraphqlConfig::class.java)

    fun config(action: Action<BluebellConfig>) {
        action.execute(config)
    }

    fun models(action: Action<BluebellModels>) {
        action.execute(models)
    }

    fun graphql(action: Action<GraphqlConfig>) {
        action.execute(graphql)
    }
}
