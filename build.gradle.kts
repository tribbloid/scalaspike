plugins {
    idea
    base
    kotlin("jvm") version "1.3.70"
    id("com.github.maiflai.scalatest").version("0.26")
}

allprojects {

    apply(plugin = "java")
    apply(plugin = "scala")

    apply(plugin = "idea")
    apply(plugin = "com.github.maiflai.scalatest")

    val vv = this.versions()

    group = "com.tribbloids.scalaspike"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
    }

    dependencies {

        implementation("org.scala-lang:scala-compiler:${vv.scalaV}")
        implementation("org.scala-lang:scala-library:${vv.scalaV}")
        implementation("org.scala-lang:scala-reflect:${vv.scalaV}")

        testImplementation("junit:junit:4.12")
        testImplementation("org.scalatest:scalatest_${vv.scalaBinaryV}:3.0.8")
        testRuntimeOnly("org.pegdown:pegdown:1.4.2")

    }

    idea.module {
        excludeDirs.add(file("warehouse"))
        excludeDirs.add(file("latex"))
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

idea {
    module {

        excludeDirs.add(file("gradle"))
    }
}
