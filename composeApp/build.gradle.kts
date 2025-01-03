plugins {
    alias(libs.plugins.nativeCocoapods)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.apolloGraphQL)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.compose.compiler)
    id("com.micrantha.bluebell")
}

kotlin {
    jvmToolchain(21)

    cocoapods {
        version = "1.0"
        summary = "Native dependencies for ${project.name}"
        homepage = "https://github.com/hackelia-micrantha/eyespie"
        license = "GPLv3"

        framework {
            baseName = "bluebell"
        }

        podfile = project.file("../iosApp/Podfile")
    }

    applyDefaultHierarchyTemplate()

    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "composeApp"
            isStatic = true
            binaryOption("bundleId", "com.micrantha.eyespie")
        }
    }

    sourceSets {

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(compose.animation)
            implementation(compose.animationGraphics)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)

            implementation(libs.kodein.di)
            implementation(libs.kodein.di.framework.compose)
            implementation(libs.kodein.di.conf)

            implementation(libs.okio)

            implementation(libs.cache4k)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.kodein)

            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.cio)

            implementation(libs.supabase.gotrue)
            implementation(libs.supabase.postgrest)
            implementation(libs.supabase.apollo.graphql)
            implementation(libs.supabase.storage)
            implementation(libs.supabase.realtime)

            implementation(libs.permissions.compose)
            implementation(libs.geo.compose)
            implementation(libs.kamel.image)
            implementation(libs.moko.media)

            //implementation("ca.rmen:rhymer:1.2.0")

            //implementation("org.hashids:hashids:1.0.3")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.ktx)

            implementation(libs.androidx.lifecycle.viewmodel.ktx)

            implementation(libs.androidx.lifecycle.runtime.ktx)

            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.fragment.ktx)

            implementation(libs.androidx.palette.ktx)

            implementation(libs.androidx.camera.core)
            implementation(libs.androidx.camera.camera2)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.camera.video)
            implementation(libs.androidx.camera.view)
            implementation(libs.androidx.camera.extensions)

            implementation(libs.tensorflow.lite)
            implementation(libs.tensorflow.lite.gpu)
            implementation(libs.tensorflow.lite.support)
            implementation(libs.tensorflow.lite.metadata)
            implementation(libs.tensorflow.lite.task.vision)
            implementation(libs.tensorflow.lite.gpu.delegate.plugin)

            implementation(libs.mediapipe.tasks.vision)
            implementation(libs.compose.ui.tooling)
        }

        iosMain.dependencies {
        }
    }
    task("testClasses")
}

android {
    namespace = "com.micrantha.eyespie"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        versionCode = 10
        versionName = "1.0.0"
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    bundle {
        language {
            enableSplit = false
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    signingConfigs {
        create("release") {
            System.getenv("ANDROID_STORE_FILE")?.let { storeFile = file(it) }
            storePassword = System.getenv("ANDROID_STORE_PASSWORD")
            keyAlias = System.getenv("ANDROID_KEY_ALIAS")
            keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "SYMBOL_TABLE"
            }
        }
    }
    dependencies {
        //releaseImplementation(files("libs/release/mobuild-envuscator.aar"))
        //debugImplementation(files("libs/debug/mobuild-envuscator.aar"))
        implementation(files("libs/debug/mobuild-envuscator.aar"))
    }
}

apollo {
    service("eyespie") {
        packageNamesFromFilePaths("com.micrantha.eyespie.graphql")
    }
}

bluebell {
    config {
        packageName = "com.micrantha.eyespie.config"
        className = "DefaultConfig"
        envFile = ".env.local"

        // Guaranteed to exist, set to null on missing file
        defaultKeys = listOf(
            "LOGIN_EMAIL",
            "LOGIN_PASSWORD"
        )
    }
    models {
        files = mapOf(
            "classification/image/efficientnet_lite0.tflite" to "classification/image.tflite",
            "detection/image/efficientdet_lite0.tflite" to "detection/image.tflite",
            "embedding/image/mobilenet_v3_small.tflite" to "embedding/image.tflite",
            "segmentation/deeplab_v3.tflite" to "segmentation/image.tflite"
        )
    }
}
