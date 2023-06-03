import java.util.*

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    kotlin("native.cocoapods")
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

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
        pod("GoogleMLKit/ImageLabeling") {
            version = "3.2.0"
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

                api("org.kodein.di:kodein-di:7.20.1")
                api("org.kodein.di:kodein-di-framework-compose:7.20.1")
                implementation("org.kodein.di:kodein-di-conf:7.19.0")

                implementation("com.squareup.okio:okio:3.3.0")

                implementation("io.github.reactivecircus.cache4k:cache4k:0.11.0")

                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

                api("cafe.adriel.voyager:voyager-navigator:1.0.0-rc06")
                implementation("cafe.adriel.voyager:voyager-transitions:1.0.0-rc06")
                implementation("cafe.adriel.voyager:voyager-kodein:1.0.0-rc06")

                implementation("io.ktor:ktor-client-cio:2.3.0")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
                implementation("io.ktor:ktor-client-logging:2.3.0")
                implementation("io.ktor:ktor-client-auth:2.3.0")

                implementation("io.github.jan-tennert.supabase:gotrue-kt:1.0.1")
                implementation("io.github.jan-tennert.supabase:postgrest-kt:1.0.1")
                implementation("io.github.jan-tennert.supabase:apollo-graphql:1.0.1")
                implementation("io.github.jan-tennert.supabase:storage-kt:1.0.1")
                implementation("io.github.jan-tennert.supabase:realtime-kt:1.0.1")

                api("dev.icerock.moko:permissions-compose:0.16.0")
                api("dev.icerock.moko:media-compose:0.11.0")
                api("dev.icerock.moko:geo-compose:0.6.0")

                api("io.github.qdsfdhvh:image-loader:1.4.4")

                //implementation("ca.rmen:rhymer:1.2.0")

                //implementation("org.hashids:hashids:1.0.3")

                implementation("com.google.mlkit:common:18.8.0")
                implementation("com.google.mlkit:vision-common:17.3.0")
                implementation("com.google.mlkit:image-labeling:17.0.7")
                implementation("com.google.mlkit:image-labeling-common:18.1.0")
                implementation("com.google.mlkit:object-detection:17.0.0")
                implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
                implementation("org.tensorflow:tensorflow-lite:2.3.0")
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
                implementation("androidx.core:core-ktx:1.10.1")

                api("androidx.compose.foundation:foundation:1.4.3")
                api("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.fragment:fragment-ktx:1.5.7")

                implementation("androidx.camera:camera-core:1.2.3")
                implementation("androidx.camera:camera-camera2:1.2.3")
                implementation("androidx.camera:camera-lifecycle:1.2.3")
                implementation("androidx.camera:camera-video:1.2.3")

                implementation("androidx.camera:camera-view:1.2.3")
                implementation("androidx.camera:camera-extensions:1.2.3")

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
        packageName.set("com.micrantha.skouter.graphql")
    }
}


// TODO: plugin
val propertyKeys = listOf(
    "apiKey", "apiDomain",
    "supaBaseKey", "supaBaseDomain",
    "userLoginEmail", "userLoginPassword",
    "keyStore", "keyStorePassword"
)

fun localProperties(): Properties {
    val properties = Properties()
    try {
        properties.load(project.rootProject.file("config.properties").reader())
    } catch (err: Throwable) {
        println("Please setup 'config.properties' with the following keys:")
        propertyKeys.forEach(::println)
    }
    return properties
}

val config = localProperties()

buildConfig {
    propertyKeys.forEach { key ->
        config[key]?.let {
            buildConfigField("String", key, "\"${it}\"")
        }
    }
}

