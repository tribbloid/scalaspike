val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

    api(project(":common"))
    testFixturesApi(testFixtures(project(":common")))

    implementation("org.typelevel:cats-effect_${vs.scala.binaryV}:3.5.0-RC3")
    implementation("org.typelevel:cats-effect-std_${vs.scala.binaryV}:3.5.0-RC3")
//    implementation("org.typelevel:cats-effect-cps_${vs.scala.binaryV}:0.4.0")
}
