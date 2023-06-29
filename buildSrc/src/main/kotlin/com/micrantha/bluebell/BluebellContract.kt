package com.micrantha.bluebell

open class BluebellConfig {
    var fileName: String = "config.properties"
    var packageName: String = "com.micrantha.bluebell"
    var className: String = "BuildConfig"

    override fun toString() = "$fileName ($packageName.$className)"
}

open class BluebellModels {
    var outputPath: String = "models"
    var ids: List<String> = emptyList()
    val files: Map<String, String> = emptyMap()
}
