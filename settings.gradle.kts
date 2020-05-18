//val versions = gradle.rootProject.versions()


include(
        "common",
        "spark"
)


pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
