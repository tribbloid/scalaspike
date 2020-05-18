val vv: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")

dependencies {

    implementation("org.apache.spark:spark-sql_${vv.scalaBinaryV}:${vv.sparkV}")
    implementation("org.apache.spark:spark-mllib_${vv.scalaBinaryV}:${vv.sparkV}")
    testImplementation("org.apache.spark:spark-yarn_${vv.scalaBinaryV}:${vv.sparkV}")

    implementation(project(":common"))
}