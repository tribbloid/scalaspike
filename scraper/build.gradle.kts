val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

    api(project(":common"))
    testFixturesApi(testFixtures(project(":common")))

// https://mvnrepository.com/artifact/net.ruippeixotog/scala-scraper
    implementation("net.ruippeixotog:scala-scraper_${vs.scala.binaryV}:3.1.0")

}
