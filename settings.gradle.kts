//val versions = gradle.rootProject.versions()

include(":prover-commons")
project(":prover-commons").projectDir = file("prover-commons/module")
include(":prover-commons:infra")
include(":prover-commons:core")
include(":prover-commons:meta2")
include(":prover-commons:spark")

include(
    ":common",
    ":common:generic",
    ":lecture",
    ":cats3",
    ":zio",
//    ":cats2",
    ":kyo",
    ":scraper",
    ":spark",
    ":serde",
    ":deeplearning"
)


pluginManagement.repositories {
    gradlePluginPortal()
    mavenLocal()
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
