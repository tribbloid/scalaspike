val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

    api(project(":graph-commons"))
    testImplementation(testFixtures(project(":graph-commons")))

    implementation("eu.timepit:singleton-ops_${vs.scalaBinaryV}:0.5.2")

    implementation("eu.timepit:refined_${vs.scalaBinaryV}:0.10.1")

    implementation("org.typelevel:cats-core_${vs.scalaBinaryV}:2.8.0")
}
