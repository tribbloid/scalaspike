plugins {
    idea
//    kotlin("jvm") version "1.3.71"
//    id("de.fayard.refreshVersions") version "0.8.7"
    base
    kotlin("jvm") version "1.3.70" apply false
    id("java")
    id("scala")
}

//val scalaV: String by project

val ext = SpikeExt(this)

allprojects {
    apply(plugin = "java")
    apply(plugin = "scala")

    group = "com.tribbloids.scalaspike"
//    artifact = "scalaspike"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
    }

    dependencies {

        implementation("org.scala-lang:scala-compiler:${ext.scalaV}")
        implementation( "org.scala-lang:scala-library:${ext.scalaV}")
        implementation( "org.scala-lang:scala-reflect:${ext.scalaV}")

//        testImplementation( "junit:junit:4.12")
//        testImplementation( "org.scalatest:scalatest_${scalaBinaryV}:3.0.8")
//
//        implementation( "com.chuusai:shapeless_${scalaBinaryV}:2.3.3")
    }
}

idea.module {
    excludeDirs.add(file("warehouse"))
    excludeDirs.add(file("gradle"))
    isDownloadJavadoc = true
    isDownloadSources = true
}
