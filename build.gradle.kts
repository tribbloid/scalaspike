buildscript {
    repositories {
        // Add here whatever repositories you're already using
        mavenCentral()
    }

    dependencies {
        classpath("ch.epfl.scala:gradle-bloop_2.12:1.6.2") // suffix is always 2.12, weird
    }
}

plugins {
//    id("ai.acyclic.java-conventions")
    id("ai.acyclic.scala2-conventions")
    id("ai.acyclic.publish-conventions")
}

subprojects {

    val vs = versions()

//    dependencies {
//
//        scalaCompilerPlugins("org.typelevel:kind-projector_${vs.scala.v}:0.13.3") // TODO: DO NOT USE! causing conflict on parsing of * infix type
//    }

    tasks {

        withType<ScalaCompile> {


            scalaCompileOptions.additionalParameters.addAll(
                listOf(


//                    "-Xsource:3",
                    "-Xsource:3-cross", // maximally similar to Scala 3
                    // quickfix should be disabled ASAP after migration
//                    "-quickfix:any",
//                        "-quickfix:cat=scala3-migration",

                    // the above "quickfix" can't handle many syntax changes, like [_] => [?] or import x._ => import x.*
//                      // in this case IntelliJ IDEA analyze/`Run inspection by name`/quickfix should be used

//                        "-quickfix:help", TODO: this doesn't work
//                        "-rewrite",
//                        "-quickfix:cat=scala3-migration"

                    "-Wconf:msg=lambda-parens:s",
                    "-Xsource-features:case-apply-copy-access",// this is the standard for Scala 3

//                    "-P:kind-projector:underscore-placeholders",

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