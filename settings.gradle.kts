//val versions = gradle.rootProject.versions()

include(":prover-commons")
project(":prover-commons").projectDir = file("prover-commons/module")
include(":prover-commons:core")
include(":prover-commons:meta2")

include(
    ":common",
    ":lecture",
    ":cats3",
//    ":cats2",
    ":scraper",
    ":spark",
    ":meta"
)


pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
