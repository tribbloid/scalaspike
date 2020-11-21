val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

    api(project(":graph-commons"))
    testImplementation(project(":common:testcommon"))

    implementation("eu.timepit:singleton-ops_${vs.scalaBinaryV}:0.5.0")

    implementation("eu.timepit:refined_${vs.scalaBinaryV}:0.9.14")

    implementation("org.typelevel:cats-core_${vs.scalaBinaryV}:2.2.0")
}
