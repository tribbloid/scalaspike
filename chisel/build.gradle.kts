val vv: Versions = versions()

dependencies {
    // Chisel3 dependency - using Maven Central coordinates
    // https://github.com/chipsalliance/chisel
    val chiselV = "3.6.1"
    implementation("edu.berkeley.cs:chisel3_${vv.scala.artifactSuffix}:$chiselV")

    // ChiselTest for testing (note: tests require Chisel compiler plugin setup)
    // https://github.com/ucb-bar/chisel-testers2
    val chiselTestV = "0.5.4"
    testImplementation("edu.berkeley.cs:chiseltest_${vv.scala.artifactSuffix}:$chiselTestV")
}
