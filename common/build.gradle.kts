val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

    api(project(":prover-commons:meta2"))
    testFixturesApi(testFixtures(project(":prover-commons:meta2")))

//    api(("com.chuusai:shapeless_${vs.scala.binaryV}:2.3.9"))
    api("eu.timepit:singleton-ops_${vs.scala.binaryV}:0.5.2")

    api("io.circe:circe-generic-extras_${vs.scala.binaryV}:0.14.3")

    implementation("eu.timepit:refined_${vs.scala.binaryV}:0.11.2")

    // https://mvnrepository.com/artifact/org.scalatestplus/scalacheck-1-17
    testImplementation("org.scalatestplus:scalacheck-1-17_${vs.scala.binaryV}:${vs.scalaTestV}.0")
}
