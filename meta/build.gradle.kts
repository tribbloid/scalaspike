val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")

dependencies {

//    implementation("org.scala-lang.virtualized:lms-clean_${vs.scalaBinaryV}:0.0.1-SNAPSHOT")

//    scalaCompilerPlugins("org.scala-lang.plugins:scala-continuations-plugin_${vs.scalaBinaryV}.2:1.0.3")

    // https://mvnrepository.com/artifact/janino/janino
    implementation("org.codehaus.janino:janino:3.1.9")

    testImplementation(testFixtures(project(":prover-commons")))
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:2.19.0")

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
