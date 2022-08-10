val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")

dependencies {

//    implementation("org.scala-lang.virtualized:lms-clean_${vs.scalaBinaryV}:0.0.1-SNAPSHOT")

//    implementation("org.typelevel:cats-core_${vs.scalaBinaryV}:2.2.0")

//    scalaCompilerPlugins("org.scala-lang.plugins:scala-continuations-plugin_${vs.scalaBinaryV}.2:1.0.3")

    // https://mvnrepository.com/artifact/janino/janino
    implementation("janino:janino:2.5.15")

    testImplementation(testFixtures(project(":graph-commons")))
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:2.18.0")

    implementation(project(":common"))
}

tasks {

    withType<ScalaCompile> {

        configureEach {

            scalaCompileOptions.apply {

                val existing: MutableList<String> = additionalParameters ?: mutableListOf()

            }
        }
    }
}
