val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")

dependencies {

//    implementation("org.scala-lang.virtualized:lms-clean_${vs.scala.binaryV}:0.0.1-SNAPSHOT")

//    scalaCompilerPlugins("org.scala-lang.plugins:scala-continuations-plugin_${vs.scala.binaryV}.2:1.0.3")

    // https://mvnrepository.com/artifact/janino/janino
    implementation("org.codehaus.janino:janino:3.1.12")


//     https://mvnrepository.com/artifact/com.github.julien-truffaut/monocle-core
    val monocleV = "3.3.0"
    implementation("dev.optics:monocle-core_${vs.scala.binaryV}:$monocleV")
    implementation("dev.optics:monocle-macro_${vs.scala.binaryV}:$monocleV")

    testImplementation(testFixtures(project(":prover-commons:meta2")))
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:2.19.0")

    implementation(project(":common"))


    val circeV = "0.14.4"

    api("io.circe:circe-parser_${vs.scala.artifactSuffix}:$circeV")
    api("io.circe:circe-generic_${vs.scala.artifactSuffix}:$circeV")
    api("io.circe:circe-generic-extras_${vs.scala.artifactSuffix}:$circeV")

    val jsoniterV = "2.38.8"

    api("com.github.plokhotnyuk.jsoniter-scala:jsoniter-scala-core_${vs.scala.artifactSuffix}:$jsoniterV")
    // Use the "provided" scope instead when the "compile-internal" scope is not supported
    api("com.github.plokhotnyuk.jsoniter-scala:jsoniter-scala-macros_${vs.scala.artifactSuffix}:$jsoniterV")

    // https://mvnrepository.com/artifact/org.apache.fory/fory-scala
    val foryV = "0.14.1"
    implementation("org.apache.fory:fory-scala_${vs.scala.artifactSuffix}:$foryV")

    testImplementation("com.google.protobuf:protobuf-java:4.33.2")

}
