val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

    api(project(":common"))
    testFixturesApi(testFixtures(project(":common")))

    implementation("org.typelevel:cats-effect_${vs.scalaBinaryV}:3.4.7")
    implementation("org.typelevel:cats-effect-std_${vs.scalaBinaryV}:3.4.7")
    implementation("org.typelevel:cats-effect-cps_${vs.scalaBinaryV}:0.4.0")
}
