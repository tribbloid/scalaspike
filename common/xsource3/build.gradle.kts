val vs: Versions = versions()

allprojects {
    tasks {

        withType<ScalaCompile> {

            scalaCompileOptions.apply {

                additionalParameters.addAll(
                    listOf(
                        "-Xsource:3"
                    )
                )
            }
        }
    }
}


dependencies {

    api(project(":common"))
}