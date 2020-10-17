//val versions = gradle.rootProject.versions()


include(
        ":graph-commons",
        ":common:testcommon",
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
