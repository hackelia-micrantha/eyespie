package com.micrantha.bluebell

open class BluebellConfig {
    var packageName: String = "com.micrantha.bluebell.config"
    var className: String = "BuildConfig"
    var envFile: String = ".env.local"
    var keys: List<String> = emptyList()
    var requiredKeys: List<String> = emptyList()
    var skip: Boolean = false
    var debugValuesOnly: Boolean = true

    override fun toString() = "($packageName)"
}

open class BluebellModels {
    var outputPaths: Array<String> = arrayOf(
        "composeApp/src/androidMain/assets/models",
        "iosApp/iosApp/Models"
    )
    var downloadPath: String = "models"

    //var ids: List<String> = emptyList()
    var files: Map<String, String> = emptyMap()
}
