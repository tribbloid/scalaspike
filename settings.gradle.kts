//val versions = gradle.rootProject.versions()


include(
        ":graph-commons",
        ":common",
        ":lecture",
        ":spark"
)


pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
