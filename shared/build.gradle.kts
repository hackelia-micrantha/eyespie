import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("com.apollographql.apollo3")
    kotlin("plugin.serialization")
    kotlin("native.cocoapods")
    id("com.micrantha.bluebell")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Skouter Shared Module"
        homepage = "https://github.com/hackelia-micrantha/skouter"
        version = "1.0.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            binaryOption("bundleId", "com.micrantha.skouter")
            isStatic = true
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
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(compose.animation)
                implementation(compose.animationGraphics)

                api("org.kodein.di:kodein-di:7.20.1")
                api("org.kodein.di:kodein-di-framework-compose:7.20.1")
                implementation("org.kodein.di:kodein-di-conf:7.19.0")

                implementation("com.squareup.okio:okio:3.3.0")

                implementation("io.github.reactivecircus.cache4k:cache4k:0.11.0")

                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

                api("cafe.adriel.voyager:voyager-navigator:1.0.0-rc06")
                implementation("cafe.adriel.voyager:voyager-transitions:1.0.0-rc06")
                implementation("cafe.adriel.voyager:voyager-kodein:1.0.0-rc06")

                implementation("io.ktor:ktor-client-cio:2.3.0")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.1")
                implementation("io.ktor:ktor-client-logging:2.3.0")
                implementation("io.ktor:ktor-client-auth:2.3.0")

                implementation("io.github.jan-tennert.supabase:gotrue-kt:1.0.4")
                implementation("io.github.jan-tennert.supabase:postgrest-kt:1.0.4")
                implementation("io.github.jan-tennert.supabase:apollo-graphql:1.0.4")
                implementation("io.github.jan-tennert.supabase:storage-kt:1.0.4")
                implementation("io.github.jan-tennert.supabase:realtime-kt:1.0.4")

                api("dev.icerock.moko:permissions-compose:0.16.0")
                api("dev.icerock.moko:geo-compose:0.6.0")
                implementation("media.kamel:kamel-image:0.6.0")

                //implementation("ca.rmen:rhymer:1.2.0")

                //implementation("org.hashids:hashids:1.0.3")
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
                api("androidx.fragment:fragment-ktx:1.6.0")

                implementation("androidx.palette:palette-ktx:1.0.0")

                implementation("androidx.camera:camera-core:1.2.3")
                implementation("androidx.camera:camera-camera2:1.2.3")
                implementation("androidx.camera:camera-lifecycle:1.2.3")
                implementation("androidx.camera:camera-video:1.2.3")
                implementation("androidx.camera:camera-view:1.2.3")
                implementation("androidx.camera:camera-extensions:1.2.3")

                implementation("com.google.mediapipe:tasks-vision:0.10.1")

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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

apollo {
    service("service") {
        packageName.set("com.micrantha.skouter.graphql")
    }
}


bluebell {
    config {
        packageName = "com.micrantha.skouter"
        className = "SkouterConfig"
        fileName = "skouter.properties"
    }
    models {
        ids = listOf("microsoft/resnet-50")
    }
}

tasks
    .withType<KotlinCompilationTask<*>>()
    .configureEach {
        compilerOptions
            .languageVersion
            .set(KotlinVersion.KOTLIN_1_9)
    }
