val vv: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")


dependencies {

//    implementation("com.chuusai:shapeless_${vv.scalaBinaryV}:2.3.3") {
//        this.setForce(true)
//    } //TODO: remove, already transitivie

    implementation("eu.timepit:singleton-ops_${vv.scalaBinaryV}:0.5.0")
    implementation("eu.timepit:refined_${vv.scalaBinaryV}:0.9.14")
}