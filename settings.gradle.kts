//val versions = gradle.rootProject.versions()

include("graph-commons")
project(":graph-commons").projectDir = file("graph-commons/core")

include(
        ":common",
        ":lecture",
        ":spark",
        ":meta"
)


pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
