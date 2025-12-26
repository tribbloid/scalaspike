val vs: Versions = versions()


dependencies {

    api(project(":common"))
    testFixturesApi(testFixtures(project(":common")))

    implementation("dev.zio:zio_${vs.scala.binaryV}:2.1.24")
}
