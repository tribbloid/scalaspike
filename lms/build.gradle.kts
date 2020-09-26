val vv: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")

dependencies {

    implementation("org.scala-lang.virtualized:lms-clean_${vv.scalaBinaryV}:0.0.1-SNAPSHOT")

    testImplementation(project(":common:testcommon"))
    implementation(project(":common"))
}