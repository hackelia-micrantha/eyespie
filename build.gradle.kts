plugins {
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    kotlin("android").apply(false)
    kotlin("multiplatform").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("com.apollographql.apollo3").version("3.8.0").apply(false)
    kotlin("plugin.serialization").version("1.8.20").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
