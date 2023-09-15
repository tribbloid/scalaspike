val vs: Versions = versions()


//apply(plugin = "java")
//apply(plugin = "scala")

dependencies {

    implementation("org.apache.spark:spark-sql_${vs.scala.binaryV}:${vs.sparkV}")
    implementation("org.apache.spark:spark-mllib_${vs.scala.binaryV}:${vs.sparkV}")

    testImplementation("org.apache.spark:spark-yarn_${vs.scala.binaryV}:${vs.sparkV}")

    implementation("org.apache.spark:spark-sql-kafka-0-10_${vs.scala.binaryV}:${vs.sparkV}")
    implementation("io.github.embeddedkafka:embedded-kafka-streams_${vs.scala.binaryV}:3.5.1")

    implementation(project(":common"))
}