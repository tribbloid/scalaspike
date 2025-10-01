val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

    api(project(":prover-commons:meta2"))
    testFixturesApi(testFixtures(project(":prover-commons:meta2")))

    api("eu.timepit:singleton-ops_${vs.scala.binaryV}:0.5.2")

    val circeVersion = "0.14.4"


    implementation("eu.timepit:refined_${vs.scala.binaryV}:0.11.3")

    // https://mvnrepository.com/artifact/org.scalatestplus/scalacheck-1-17
    testImplementation("org.scalatestplus:scalacheck-1-18_${vs.scala.binaryV}:${vs.scalaTestV}.0")
}
