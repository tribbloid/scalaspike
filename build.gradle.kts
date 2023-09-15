plugins {
//    base
    java
    `java-library`
    `java-test-fixtures`

    scala
    id("io.github.cosmicsilence.scalafix") version "0.1.14"

    idea

    id("com.github.ben-manes.versions") version "0.44.0"
}

allprojects {

    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "java-test-fixtures")

    apply(plugin = "scala")
    apply(plugin = "io.github.cosmicsilence.scalafix")

    apply(plugin = "idea")

    val vs = this.versions()

    group = "com.tribbloids.scalaspike"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
//        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
    }

//    configurations.all {
//        resolutionStrategy.dependencySubstitution {
//            substitute(module("com.chuusai:shapeless_${vs.scala.binaryV}")).apply {
//                with(module("com.chuusai:shapeless_${vs.scala.binaryV}:2.3.3"))
//            }
//        }
//    }

    dependencies {

        // see https://github.com/gradle/gradle/issues/13067
        fun both(notation: Any) {
            implementation(notation)
            testFixturesImplementation(notation)
        }

        implementation("${vs.scala.group}:scala-library:${vs.scala.v}")

        implementation("org.scala-lang:scala-compiler:${vs.scala.v}")
        implementation("org.scala-lang:scala-reflect:${vs.scala.v}")

//        testRuntimeOnly("org.junit.platform:junit-platform-engine:$jUnitPlatformV")
//        testRuntimeOnly("org.junit.platform:junit-platform-launcher:$jUnitPlatformV")

        // TODO: alpha project, switch to mature solution once https://github.com/scalatest/scalatest/issues/1454 is solved
        testRuntimeOnly("co.helmethair:scalatest-junit-runner:0.2.0")

        testFixturesApi("org.scalatest:scalatest_${vs.scala.binaryV}:${vs.scalaTestV}")
        // https://mvnrepository.com/artifact/org.scalatestplus/scalacheck-1-17
        testImplementation("org.scalatestplus:scalacheck-1-17_2.13:${vs.scalaTestV}.0")

        testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

//        testRuntimeOnly("org.pegdown:pegdown:1.4.2") // required by maiflai scalatest

        if (vs.splainV.isNotEmpty()) {
            val splainD = "io.tryp:splain_${vs.scala.v}:${vs.splainV}"
            logger.warn("Using " + splainD)

            scalaCompilerPlugins(splainD)
        }
    }

    // see https://stackoverflow.com/questions/44266687/how-to-print-out-all-dependencies-in-a-gradle-multi-project-build
    task("dependencyTree") {

        dependsOn("dependencies")
    }

    tasks {

//        val jvmTarget = JavaVersion.VERSION_1_8.toString()

//        scala {
//            this.zincVersion
//        }

        withType<ScalaCompile> {

            configureEach {

//                targetCompatibility = jvmTarget

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
//                                    "-Xlog-implicit-conversions",

//                                    "-Yissue-debug"

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

        module {

            excludeDirs = excludeDirs + files(

                "target",
                "out",
                "build",

                ".idea",
                ".vscode",
                ".bloop",
                ".bsp",
                ".metals",
                "bin",

                ".ammonite",

                "logs",

                )

            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }
}


idea {

    targetVersion = "2020"


    module {
        excludeDirs.add(file("prover-commons"))

        // apache spark
        excludeDirs.add(file("warehouse"))
        excludeDirs.add(file("latex"))

        // gradle log
        excludeDirs.add(file("logs"))
        excludeDirs.add(file("gradle"))
    }
}