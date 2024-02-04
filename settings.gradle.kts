//val versions = gradle.rootProject.versions()

include(":prover-commons")
project(":prover-commons").projectDir = file("prover-commons/module")
include(":prover-commons:core")
include(":prover-commons:meta2")
include(":prover-commons:spark")

include(
    ":common",
    ":common:xsource3",
    ":lecture",
    ":cats3",
//    ":cats2",
    ":scraper",
    ":spark",
    ":meta"
)


pluginManagement.repositories {
    gradlePluginPortal()
    mavenLocal()
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
