val vs: Versions = versions()


dependencies {

    api(project(":common"))
    testFixturesApi(testFixtures(project(":common")))

    val zioV = "2.1.24"

    implementation("dev.zio:zio_${vs.scala.binaryV}:$zioV")
//    implementation("dev.zio:zio-direct_${vs.scala.binaryV}:1.0.0-RC7")

    // Quill for compile-time query generation and AST extraction
    implementation("io.getquill:quill-sql_${vs.scala.binaryV}:4.8.5")

    implementation("dev.zio:zio-blocks-schema_2.13:0.0.11")

    testImplementation("dev.zio:zio-test_2.13:$zioV")
    testImplementation("dev.zio:zio-test-sbt_2.13:$zioV")
}
