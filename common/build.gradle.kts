val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

    api(project(":prover-commons"))
    testImplementation(testFixtures(project(":prover-commons")))

//    api(("com.chuusai:shapeless_${vs.scalaBinaryV}:2.3.9"))
    api("eu.timepit:singleton-ops_${vs.scalaBinaryV}:0.5.2")

    api("io.circe:circe-generic-extras_${vs.scalaBinaryV}:0.14.2")

    implementation("eu.timepit:refined_${vs.scalaBinaryV}:0.10.1")

    implementation("org.typelevel:cats-effect_${vs.scalaBinaryV}:2.5.5")
}
