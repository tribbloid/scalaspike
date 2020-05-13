//import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies

include(
        "spark"
)

pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}

//buildscript {
//    repositories { gradlePluginPortal() }
//    dependencies.classpath("de.fayard:dependencies:0.5.8")
//}
//
//bootstrapRefreshVersionsAndDependencies()
