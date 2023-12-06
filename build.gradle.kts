buildscript {
    repositories {
        // Add here whatever repositories you're already using
        mavenCentral()
    }
}

plugins {
//    id("ai.acyclic.java-conventions")
    id("ai.acyclic.scala2-conventions")
    id("ai.acyclic.publish-conventions")
}

subprojects {


    tasks {

        withType<ScalaCompile> {

            scalaCompileOptions.additionalParameters.addAll(
                listOf(

                    "-Wconf:cat=deprecation:ws"
                )
            )
        }
    }
}


idea {

    module {
        excludeDirs.add(file("prover-commons"))

        // apache spark
        excludeDirs.add(file("warehouse"))

        excludeDirs.add(file("latex"))
    }
}