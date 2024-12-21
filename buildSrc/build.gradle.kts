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
    implementation(libs.build.config)
    implementation(libs.dotenv.kotlin)

    implementation(gradleApi())
    implementation(localGroovy())
}
