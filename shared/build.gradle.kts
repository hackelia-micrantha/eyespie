import java.util.*

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("com.apollographql.apollo3")
    id("com.github.gmazzo.buildconfig") version "3.1.0"
    kotlin("plugin.serialization") version "1.8.20"
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.ui)
                api(compose.foundation)
                api(compose.materialIconsExtended)
                api(compose.material3)

                api("io.insert-koin:koin-core:3.4.0")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                runtimeOnly("org.jetbrains.skiko:skiko:0.7.58")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

                implementation("com.chrynan.navigation:navigation-compose:0.7.0")

                implementation("io.ktor:ktor-client-cio:2.3.0")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
                implementation("com.soywiz:korim:0.19.1")

                implementation("io.github.jan-tennert.supabase:gotrue-kt:0.9.3")
                implementation("io.github.jan-tennert.supabase:postgrest-kt:0.9.3")
                implementation("io.github.jan-tennert.supabase:apollo-graphql:0.9.3")
                implementation("io.github.jan-tennert.supabase:storage-kt:0.9.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.appcompat:appcompat:1.6.1")
                implementation("androidx.core:core-ktx:1.10.0")
                implementation("io.insert-koin:koin-androidx-compose:3.4.3")
                //implementation("org.jetbrains.kotlinx:kotlin-deeplearning-api:0.5.1")

                api("androidx.compose.foundation:foundation:1.4.2")

                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

                //implementation("io.github.chopyourbrain:kontrol:0.1.1")
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.micrantha.skouter"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
}

apollo {
    service("service") {
        packageName.set("com.micrantha.skouter")
    }
}

fun localProperties(): Properties {
    val properties = Properties()
    properties.load(project.rootProject.file("config.properties").reader())
    return properties
}

val config = localProperties()

buildConfig {
    listOf(
        "apiKey", "apiDomain",
        "supaBaseKey", "supaBaseDomain",
        "userLoginEmail", "userLoginPassword",
        "keyStore", "keyStorePassword"
    ).forEach { key ->
        config[key]?.let {
            buildConfigField("String", key, "\"${it}\"")
        }
    }
}

