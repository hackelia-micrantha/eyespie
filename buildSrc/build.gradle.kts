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
    implementation("com.github.gmazzo:gradle-buildconfig-plugin:3.1.0")

    implementation(gradleApi())
    implementation(localGroovy())
}
