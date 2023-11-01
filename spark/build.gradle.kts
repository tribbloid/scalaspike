val vs = versions()
val sparkV = "3.5.0"

dependencies {

    implementation("org.apache.spark:spark-sql_${vs.scala.binaryV}:${sparkV}")
    implementation("org.apache.spark:spark-mllib_${vs.scala.binaryV}:${sparkV}")

    implementation("org.typelevel:frameless-dataset_${vs.scala.binaryV}:0.15.0")

    testImplementation("org.apache.spark:spark-yarn_${vs.scala.binaryV}:${sparkV}")

    implementation("org.apache.spark:spark-sql-kafka-0-10_${vs.scala.binaryV}:${sparkV}")
    implementation("io.github.embeddedkafka:embedded-kafka-streams_${vs.scala.binaryV}:3.5.1")

    implementation(project(":common"))
}