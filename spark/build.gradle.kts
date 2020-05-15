val vv: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")

dependencies {

    implementation("org.apache.spark:spark-sql_${vv.scalaBinaryV}:${vv.sparkV}")
    implementation("org.apache.spark:spark-mllib_${vv.scalaBinaryV}:${vv.sparkV}")
    testImplementation("org.apache.spark:spark-yarn_${vv.scalaBinaryV}:${vv.sparkV}")

    implementation("org.apache.spark:spark-sql-kafka-0-10_${vv.scalaBinaryV}:${vv.sparkV}")
    implementation("io.github.embeddedkafka:embedded-kafka-streams_${vv.scalaBinaryV}:2.4.1")

    implementation(project(":common"))
}