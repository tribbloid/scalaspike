plugins {
//    base
    java
    scala
    idea
}

val jUnitV = "5.6.2"
val jUnitPlatformV = "1.6.0" // TODO: useless

allprojects {

    apply(plugin = "base")
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "java-test-fixtures")
    apply(plugin = "scala")
    apply(plugin = "idea")

    val vs = this.versions()

    group = "com.tribbloids.scalaspike"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
//        maven("https://dl.bintray.com/kotlin/kotlin-dev")
    }

    configurations.all {
        resolutionStrategy.dependencySubstitution {
            substitute(module("com.chuusai:shapeless_${vs.scalaBinaryV}")).apply {
                with(module("com.chuusai:shapeless_${vs.scalaBinaryV}:2.3.3"))
            }
        }
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

        testImplementation("org.scalatest:scalatest_${vs.scalaBinaryV}:${vs.scalatestV}")
//        testRuntimeOnly("org.pegdown:pegdown:1.4.2") // required by maiflai scalatest

    }

    // see https://stackoverflow.com/questions/44266687/how-to-print-out-all-dependencies-in-a-gradle-multi-project-build
    task("dependencyTree") {

        dependsOn("dependencies")
    }

    tasks {


        val jvmTarget = JavaVersion.VERSION_1_8.toString()

//        scala {
//            this.zincVersion
//        }

        withType<ScalaCompile> {

            configureEach {

                targetCompatibility = jvmTarget

                scalaCompileOptions.apply {

//                    isForce = true
                    loggingLevel = "verbose"

                    val existing: MutableList<String> = additionalParameters ?: mutableListOf()

                    additionalParameters = existing.plus(
                            listOf(
                                    "-encoding", "utf8",
                                    "-unchecked",
                                    "-deprecation",
                                    "-feature",
//                            "-Xfatal-warnings",

                                    "-Xlint:poly-implicit-overload",
                                    "-Xlint:option-implicit",

                                    "-Xlog-implicits",
                                    "-Xlog-implicit-conversions",
                                    "-Yissue-debug"

                                    // the following only works on scala 2.13
//                        ,
//                        "-Xlint:implicit-not-found",
//                        "-Xlint:implicit-recursion"
                            )
                    )

                    forkOptions.apply {

                        memoryInitialSize = "1g"
                        memoryMaximumSize = "4g"

                        // this may be over the top but the test code in macro & core frequently run implicit search on church encoded Nat type
                        jvmArgs = listOf(
                                "-Xss256m"
                        )
                    }
                }
            }
        }


        test {

            minHeapSize = "1024m"
            maxHeapSize = "4096m"

            useJUnitPlatform {
                includeEngines("scalatest")
                testLogging {
                    events("passed", "skipped", "failed")
                }
            }

            testLogging {
//                events = setOf(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED, org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED, org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED, org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT)
//                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
            }
        }
    }

    idea {

        targetVersion = "2020"


        module {

            // apache spark
            excludeDirs.add(file("warehouse"))
            excludeDirs.add(file("latex"))

            // gradle log
            excludeDirs.add(file("logs"))
            excludeDirs.add(file("gradle"))

            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }
}

