plugins {
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.apolloGraphQL) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.nativeCocoapods) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
