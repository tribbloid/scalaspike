val vv: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

    implementation("eu.timepit:singleton-ops_${vv.scalaBinaryV}:0.5.0")
    implementation("eu.timepit:refined_${vv.scalaBinaryV}:0.9.14")
}