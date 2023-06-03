plugins {
    id("com.android.application").version("7.4.2").apply(false)
    id("com.android.library").version("7.4.2").apply(false)
    kotlin("android").version("1.8.20").apply(false)
    kotlin("multiplatform").version("1.8.20").apply(false)
    id("org.jetbrains.compose").version("1.4.0").apply(false)
    id("com.apollographql.apollo3").version("3.8.0").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
