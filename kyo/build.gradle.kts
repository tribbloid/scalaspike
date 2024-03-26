val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

    api(project(":common"))
    testFixturesApi(testFixtures(project(":common")))

    // https://mvnrepository.com/artifact/io.getkyo/kyo-core
    implementation("io.getkyo:kyo-core_${vs.scala.binaryV}:0.8.5")
}
