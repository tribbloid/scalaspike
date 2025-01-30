val vs: Versions = versions()

dependencies {

    api(project(":common"))

    val circeV = "0.14.4"

    api("io.circe:circe-parser_${vs.scala.artifactSuffix}:$circeV")
    api("io.circe:circe-generic_${vs.scala.artifactSuffix}:$circeV")
    api("io.circe:circe-generic-extras_${vs.scala.artifactSuffix}:$circeV")


    val jsoniterV = "2.33.1"


    api("com.github.plokhotnyuk.jsoniter-scala:jsoniter-scala-core_${vs.scala.artifactSuffix}:$jsoniterV")

    // Use the "provided" scope instead when the "compile-internal" scope is not supported
    api("com.github.plokhotnyuk.jsoniter-scala:jsoniter-scala-macros_${vs.scala.artifactSuffix}:$jsoniterV")
}