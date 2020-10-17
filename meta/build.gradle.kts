val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")

dependencies {

    implementation("org.scala-lang.virtualized:lms-clean_${vs.scalaBinaryV}:0.0.1-SNAPSHOT")

//    implementation("org.scala-lang.plugins:scala-continuations-library_${vv.scalaBinaryV}:1.0.3")

    scalaCompilerPlugins("org.scala-lang.plugins:scala-continuations-plugin_${vs.scalaBinaryV}.2:1.0.3")

    // https://mvnrepository.com/artifact/janino/janino
    implementation("janino:janino:2.5.10")

    testImplementation(project(":common:testcommon"))
    implementation(project(":common"))
}

tasks {

    withType<ScalaCompile> {

        configureEach {

            scalaCompileOptions.apply {

                val existing: MutableList<String> = additionalParameters ?: mutableListOf()

                additionalParameters = existing.plus(
                        listOf(
                                "-P:continuations:enable"
                        )
                )
            }
        }
    }
}