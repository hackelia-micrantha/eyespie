package com.micrantha.bluebell

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

open class BluebellExtension @Inject constructor(
    objects: ObjectFactory
) {
    val config = objects.newInstance(BluebellConfig::class.java)

    val assets = objects.newInstance(BluebellAssets::class.java)

    val graphql = objects.newInstance(GraphqlConfig::class.java)

    fun config(action: Action<BluebellConfig>) {
        action.execute(config)
    }

    fun assets(action: Action<BluebellAssets>) {
        action.execute(assets)
    }

    fun graphql(action: Action<GraphqlConfig>) {
        action.execute(graphql)
    }
}
