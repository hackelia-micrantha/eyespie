plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.4.2").apply(false)
    id("com.android.library").version("7.4.2").apply(false)
    kotlin("android").version("1.8.10").apply(false)
    kotlin("multiplatform").version("1.8.10").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("com.apollographql.apollo3").version("3.8.0").apply(false)
}

fun localProperties(): java.util.Properties {
    val properties = java.util.Properties()
    properties.load(project.rootProject.file("local.properties").reader())
    return properties
}

val properties = localProperties()

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
