//val versions = gradle.rootProject.versions()

include("prover-commons")
project(":prover-commons").projectDir = file("prover-commons/core")

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
