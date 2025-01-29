val vs: Versions = versions()

dependencies {

    api(project(":common"))

    val circeVersion = "0.14.4"

    api("io.circe:circe-parser_${vs.scala.artifactSuffix}:$circeVersion")
    api("io.circe:circe-generic_${vs.scala.artifactSuffix}:$circeVersion")
    api("io.circe:circe-generic-extras_${vs.scala.artifactSuffix}:$circeVersion")
}