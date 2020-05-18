val vv: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")

dependencies {

    implementation("com.chuusai:shapeless_${vv.scalaBinaryV}:2.3.3")
}