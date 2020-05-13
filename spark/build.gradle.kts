
//plugins{
//
//}
//
//subprojects {
//}

//apply("$rootDir/build.gradle.kts")

println("$rootDir/build.gradle.kts")

apply {
    from("$rootDir/build.gradle.kts")
    from("$rootDir/shared.kts")
}

val sparkV : String by project

//val v: Build_gradle = this

dependencies {

//    implementation( "org.apache.spark:spark-sql_${scalaBinaryV}:${sparkV}")
//    implementation( "org.apache.spark:spark-mllib_${scalaBinaryV}:${sparkV}")
//    testImplementation( "org.apache.spark:spark-yarn_${scalaBinaryV}:${sparkV}")
}