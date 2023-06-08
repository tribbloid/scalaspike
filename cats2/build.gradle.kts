val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

    api(project(":common"))
    testFixturesApi(testFixtures(project(":common")))

    implementation("org.typelevel:cats-effect_${vs.scala.binaryV}:2.5.5")
}
