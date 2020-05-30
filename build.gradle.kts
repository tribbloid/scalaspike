plugins {
    idea
    base
    kotlin("jvm") version "1.3.70"
    id("com.github.maiflai.scalatest").version("0.26")
}

val jUnitV = "5.6.2"
val jUnitPlatformV = "1.6.0" // TODO: useless

allprojects {

    apply(plugin = "java")
    apply(plugin = "scala")

    apply(plugin = "idea")

    val vs = this.versions()

    group = "com.tribbloids.scalaspike"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
    }

    dependencies {

        implementation("org.scala-lang:scala-compiler:${vs.scalaV}")
        implementation("org.scala-lang:scala-library:${vs.scalaV}")
        implementation("org.scala-lang:scala-reflect:${vs.scalaV}")

//        testImplementation("junit:junit:4.12")
        testImplementation("org.junit.jupiter:junit-jupiter:$jUnitV")

//        testRuntimeOnly("org.junit.platform:junit-platform-engine:$jUnitPlatformV")
//        testRuntimeOnly("org.junit.platform:junit-platform-launcher:$jUnitPlatformV")

        // TODO: alpha project, switch to mature solution once https://github.com/scalatest/scalatest/issues/1454 is solved
        testRuntimeOnly("co.helmethair:scalatest-junit-runner:0.1.3")

        testImplementation("org.scalatest:scalatest_${vs.scalaBinaryV}:3.0.8")
//        testRuntimeOnly("org.pegdown:pegdown:1.4.2") // required by maiflai scalatest

    }

    idea.module {
        excludeDirs.add(file("warehouse"))
        excludeDirs.add(file("latex"))
        isDownloadJavadoc = true
        isDownloadSources = true
    }


    tasks {

        test {

            useJUnitPlatform {
                includeEngines("scalatest")
                testLogging {
                    events("passed", "skipped", "failed")
                }
            }
        }
    }

    // scalatest plugin
//    apply(plugin = "com.github.maiflai.scalatest")
//
//    tasks {
//
//        test {
//
//            val configMap = extensions.getByName("configMap")
//            configMap.closureOf<MutableMap<String, Any?>> {
//                this["W"] = null
//
//                println(this)
//            }
//
////            ScalaTestPlugin.setMODE("append")
//        }
//    }

}

idea {
    module {

        excludeDirs.add(file("gradle"))
    }
}