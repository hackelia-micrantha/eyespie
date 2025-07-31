package com.micrantha.bluebell

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.domainObjectContainer
import org.gradle.kotlin.dsl.property
import javax.inject.Inject


open class BluebellConfig {
    var packageName: String = "com.micrantha.bluebell.config"
    var className: String = "BuildConfig"
    var envFile: String = ".env.local"
    var defaultKeys: List<String> = emptyList()
    var requiredKeys: List<String> = emptyList()

    internal var properties: Map<String, String> = emptyMap()

    override fun toString() = "($packageName)"
}

open class GraphqlConfig {
    var serviceName: String = ""
    var packagePath: String? = null
    var endpoint: String = "http://localhost:8080"
    var headers: Map<String, String> = emptyMap()
}

abstract class BluebellAssets @Inject constructor(objects: ObjectFactory) {
    var downloads = objects.domainObjectContainer(BluebellDownload::class)
    val copies: List<BluebellCopy> = emptyList()
}

abstract class BluebellDownload @Inject constructor(val name: String) {
    abstract var url: String
    abstract var destination: String
}

//data class BluebellDownload(
//    val url: String,
//    val destination: String,
//    val id: String
//)

data class BluebellCopy(
    val source: String,
    val destination: String,
    val id: String
)


