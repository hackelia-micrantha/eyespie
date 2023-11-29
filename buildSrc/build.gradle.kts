plugins {
    `kotlin-dsl`
}

repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    //implementation("com.android.tools.build:gradle:8.1.4")
    //implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
    implementation("com.github.gmazzo:gradle-buildconfig-plugin:3.1.0")

    implementation(gradleApi())
    implementation(localGroovy())
}